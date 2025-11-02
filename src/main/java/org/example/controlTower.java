package org.example;

    class RunwayControl {

        boolean threadInterrupted = false;

        public synchronized void takeOff(String flightName) {

            if(threadInterrupted){
                Thread.currentThread().interrupt();
            }
            System.out.println("🛫 " + flightName + " is **TAKING OFF**...");

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                System.out.println("⚠️ " + flightName + " was interrupted during takeoff! Emergency stop.");
                threadInterrupted = true;
                return;
            }

            System.out.println("🛬 " + flightName + " is **LEAVING THE RUNWAY**. (Runway Free)");

        }
    }

    class Airplane implements Runnable {
        private final String flightName;
        private final RunwayControl runway;

        public Airplane(String flightName, RunwayControl runway) {
            this.flightName = flightName;
            this.runway = runway;
        }

        @Override
        public void run() {
            System.out.println("💬 " + flightName + ": **REQUESTING RUNWAY ACCESS** (Priority: " +
                    Thread.currentThread().getPriority() + ")");
            runway.takeOff(flightName);
        }
    }


    public class controlTower {

        public static void main(String[] args) throws InterruptedException {
            System.out.println("=== Control Tower (Main Thread) Initiating Operations ===");

            RunwayControl runway = new RunwayControl();

            Thread mas01 = new Thread(new Airplane("Flight MAS01 EL-MARIACHI", runway), "MAS01");
            Thread mas02 = new Thread(new Airplane("Flight MAS02 EUDORA", runway), "MAS02");
            Thread mas03 = new Thread(new Airplane("Flight MAS03 DEJAVU", runway), "MAS03");
            Thread mas04 = new Thread(new Airplane("Flight MAS04 UTOPIA", runway), "MAS04");

            mas01.setPriority(Thread.MAX_PRIORITY); // VIP
            mas03.setPriority(Thread.MAX_PRIORITY); // Emergency
            mas02.setPriority(Thread.NORM_PRIORITY); // Normal
            mas04.setPriority(Thread.MIN_PRIORITY); // Low-priority

            System.out.println("MAS01 (VIP) Priority: " + mas01.getPriority());
            System.out.println("MAS02 (Normal) Priority: " + mas02.getPriority());
            System.out.println("MAS03 (Emergency) Priority: " + mas03.getPriority());
            System.out.println("MAS04 (Low-P) Priority: " + mas04.getPriority()+"\n");

            mas01.start();
            mas02.start();
            mas03.start();
            mas04.start();

            mas04.interrupt();

            mas01.join();
            mas02.join();
            mas03.join();
            mas04.join();

            System.out.println("\n⚠️ " + "Flight MAS04 UTOPIA is delayed due to heavy rain");

            System.out.println("\n*** All flights completed. Control Tower closing operations. ***");
        }
    }