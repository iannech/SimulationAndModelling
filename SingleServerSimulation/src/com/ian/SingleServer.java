package com.ian;


/**
 * Created by ian on 4/15/2016.
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
public class SingleServer {
    private final int Q_LIMIT = 100; // Limit for the queue length
    private final int BUSY = 1; // Mnemonics for server's being busy
    private final int IDLE = 0; // and idle.

    private int nextEventType, numCustsDelayed, numDelaysRequired, numEvents, numInQ, serverStatus;
    private float areaNumInQ, areaServerStatus, meanInterarrival, meanService, simTime, timeLastEvent, totalOfDelays;
    private float timeArrival[] = new float[Q_LIMIT + 1];
    private float timeNextEvent[] = new float[3];

    private final String IN_FILE = "mm1.in";
    private final String OUT_FILE = "mm1.out";

    private StringBuilder buffer = new StringBuilder();

    public void launch() {

        /* Specify the number of events for the timing function */
        numEvents = 2;

        /* Read input parameters */
        readInputParams();

        /* Write report heading and input parameters */
        buffer.append("Single server queuing system\n\n");
        buffer.append(String.format("Mean inter arrival time%11.3f minutes \n\n", meanInterarrival));
        buffer.append(String.format("Mean service time%16.3f minutes\n\n", meanService));
        buffer.append(String.format("Number of customers%14d\n\n", numDelaysRequired));

        /*Initialize the simulation */
        initialize();

        /* Run the simulation while more delays are still needed */
        while (numCustsDelayed < numDelaysRequired) {
            /*Determine the next event */
            timing();

            /* Update time-average statistical accumulators. */
            update_time_avg_stats();

            /* Invoke the appropriate event function */
            switch (nextEventType) {
                case 1:
                    arrive();
                    break;
                case 2:
                    depart();
                    break;
            }
        }

        /* Invoke the report generator and end the simulation */
        report();
        writeOutput();
    }

    /**
     * Read input params from input file
     */
    private void readInputParams() {
        try {
            String input = Files.readAllLines(Paths.get(IN_FILE)).get(0);
            String[] params = input.split("\\s+");
            meanInterarrival = Float.valueOf(params[0]);
            meanService = Float.valueOf(params[1]);
            numDelaysRequired = Integer.valueOf(params[2]);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private void initialize() { /* Initialize function. */
        /* Initialize the simulation clock. */
        simTime = 0;

        /* Initialize the state variables. */
        serverStatus = IDLE;
        numInQ = 0;
        timeLastEvent = 0.0f;

        /* Initialize the statistical counters. */
        numCustsDelayed = 0;
        totalOfDelays = 0.0f;
        areaNumInQ = 0.0f;
        areaServerStatus = 0.0f;

        /* Initialize the event list. Since no customers are present,
	    the departure (service completion) event is eliminated from consideration. */
        timeNextEvent[1] = simTime + expon(meanInterarrival);
        timeNextEvent[2] = (float) 1.0e+30;
    }

    private void timing() { /* Timing function. */
        int i;
        float minTimeNextEvent = (float) 1.0e+29;

        nextEventType = 0;

        /* Determine the event type for the next event to occur. */
        for (i = 1; i <= numEvents; ++i) {
            if (timeNextEvent[i] < minTimeNextEvent) {
                minTimeNextEvent = timeNextEvent[i];
                nextEventType = i;
            }
        }

        /* Check to see whether the event list is empty. */
        if (nextEventType == 0) {
		    /* The event list is empty, so stop the simulation. */
            buffer.append(String.format("\nEvent list is empty at time %f", simTime));
            writeOutput();
            System.exit(1);
        }

        /* The event list is not empty, so advance the simulation clock. */
        simTime = minTimeNextEvent;
    }

    private void arrive() { /* Arrival event function. */
        float delay;
        /* Schedule next arrival. */
        timeNextEvent[1] = simTime + expon(meanInterarrival);

        /* Check to see whether server is busy. */
        if (serverStatus == BUSY) {
		        /* Server is busy so increment the number of customers in the queue. */
            ++numInQ;

            /* Check to see whether an overflow condition exists. */
            if (numInQ > Q_LIMIT) {
			        /* The queue has overflowed, stop the simulation. */
                buffer.append("\nOverflow of the array time_arrival at");
                buffer.append(String.format(" time %f", simTime));
                System.exit(2);
            }

            /* There is still room in the queue, store the time of arrival
		    of the arriving customer at the (new) end of time_arrival. */
            timeArrival[numInQ] = simTime;
        } else {
            /* Server is idle, so arriving customer has a delay of zero. */
            delay = 0.0f;
            totalOfDelays += delay;

            /* Increment the number of customers delayed, and make server busy. */
            ++numCustsDelayed;
            serverStatus = BUSY;

            /* Schedule a departure (service completion). */
            timeNextEvent[2] = simTime + expon(meanService);
        }
    }

    private void depart() { /*Departure event function. */
        int i;
        float delay;

        /* Check to see whether the queue is empty. */
        if (numInQ == 0) {
            /* The queue is empty so make the server idle and eliminate the
		    departure (service completion) event from consideration. */
            serverStatus = IDLE;
            timeNextEvent[2] = (float) 1.0e+30;
        } else {
            /* The queue is nonempty, so decrement the number of customers in queue. */
            --numInQ;

            /* Compute the delay of the customer who is beginning service
		    and update the total delay of accumulator. */
            delay = simTime - timeArrival[1];
            totalOfDelays += delay;

            /* Increment the number of customers delayed, and schedule departure. */
            ++numCustsDelayed;
            timeNextEvent[2] = simTime + expon(meanService);

            /* Move each customer in queue (if any) up one place. */
            for (i = 1; i <= numInQ; ++i) {
                timeArrival[i] = timeArrival[i + 1];
            }
        }
    }

    private void report() { /*Report generator function */
        /* Compute and write estimates of desired measures of performance. */
        buffer.append(String.format("\nAverage delay in queue%11.3f minutes\n\n", totalOfDelays / numCustsDelayed));
        buffer.append(String.format("\nAverage number in queue%10.3f\n\n", areaNumInQ / simTime));
        buffer.append(String.format("Server utilization%15.3f\n\n", areaServerStatus / simTime));
        buffer.append(String.format("Time simulation ended%12.3f minutes", simTime));
    }

    private void update_time_avg_stats() { /* Update area accumulators for time-average statistics. */
        float time_since_last_event;

        /* Compute time since last event, and update last-event-time- marker. */
        time_since_last_event = simTime - timeLastEvent;
        timeLastEvent = simTime;

        /* Update area under number-in-queue function */
        areaNumInQ += numInQ * time_since_last_event;

        /* Update area under server-busy indicator function. */
        areaServerStatus += serverStatus * time_since_last_event;
    }

    /**
     * Writes output to the mm1.out file
     */
    private void writeOutput() {
        try (PrintWriter out = new PrintWriter(OUT_FILE)) {
            out.println(buffer.toString());
        } catch (FileNotFoundException e) {
            System.err.println(e.toString());
        }
    }

    private float expon(float mean) {
        /* generate a random variable */
        double x = Math.random();
        return (float)(-mean * Math.log(x));
    }
}

