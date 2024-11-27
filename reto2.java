import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class reto2 {

    static String[] planet = {"Marte", "Júpiter", "Venus"};
    static double[] distance = {225.0, 536.0, 41.4};
    static String[] ships = {"Nave A", "Nave B", "Nave C"};
    static double[] speeds = {30.0, 25.0, 40.0}; // Velocidades en millones de km por día
    static int[] capacities = {5, 2, 1}; // Capacidades en número de pasajeros, Nave C solo permite 1 pasajero
    static double[] fuelLevels = {100.0, 100.0, 100.0}; // Niveles iniciales de combustible en %
    static double[] oxygenLevels = {500.0, 1000.0, 800.0}; // Niveles de oxígeno en litros, Nave A tiene el menor oxígeno

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String selectedPlanet = null;
        double selectedDistance = 0;
        String selectedShip = null;
        double selectedSpeed = 0;
        double selectedOxygen = 0;
        int chosenShip = -1;
        int numberOfPassengers = 0;
        int oxygenAmount = 0;
        int fuel = 0;

        System.out.printf("Bienvenido al simulador de viajes espaciales, ten en cuenta%n el nivel de combustible, oxígeno y cuidado con los eventos aleatorios%n");

        while (true) {
            System.out.println("1. Seleccionar un planeta");
            System.out.println("2. Seleccionar la nave");
            System.out.println("3. Iniciar");
            System.out.println("4. Salir");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    int chosenPlanet = selectPlanet(scanner);
                    selectedPlanet = planet[chosenPlanet];
                    selectedDistance = distance[chosenPlanet];
                    System.out.println("Has seleccionado " + selectedPlanet + " con una distancia de " + selectedDistance + " millones de km.");
                    break;
                case 2:
                    System.out.print("Cantidad de pasajeros: ");
                    numberOfPassengers = scanner.nextInt();
                    chosenShip = selectShip(scanner, numberOfPassengers);
                    selectedShip = ships[chosenShip];
                    selectedSpeed = speeds[chosenShip];
                    selectedOxygen = oxygenLevels[chosenShip];
                    
                    if (selectedPlanet != null && selectedPlanet.equals("Júpiter") && selectedShip.equals("Nave A")) {
                        System.err.println("La nave A no tiene suficiente oxígeno para llegar a Júpiter. Por favor, selecciona otra nave o planeta.");
                        chosenShip = -1; // Resetear la selección de nave
                    } else {
                        System.out.println("Has seleccionado " + selectedShip + " con una velocidad de " + selectedSpeed + " millones de km por día.");
                        double travelTime = calculateTravelTime(selectedDistance, selectedSpeed);
                        System.out.printf("El viaje a %s con %s tardará aproximadamente %.2f días.%n", selectedPlanet, selectedShip, travelTime);
                    }
                    break;
                case 3:
                    if (selectedPlanet == null || selectedShip == null || chosenShip == -1) {
                        System.err.println("Por favor, selecciona un planeta y una nave antes de iniciar.");
                    } else {
                        System.out.println("Iniciando el viaje a " + selectedPlanet + " con " + selectedShip);
                        startJourney(scanner, selectedDistance, selectedSpeed, fuelLevels[chosenShip], selectedOxygen);
                    }
                    break;
                case 4:
                    scanner.close();
                    System.out.println("Saliendo...");
                    System.out.println("Salida con éxito");
                    System.exit(0); 
                default:
                    System.err.println("Opción inválida");
                    break;
            }
        }
    }

    public static int selectPlanet(Scanner scanner) {
        for (int i = 0; i < planet.length; i++) {
            System.out.println((i + 1) + ". " + planet[i]);
        }

        int choice = -1;
        try {
            choice = scanner.nextInt();
            if (choice > 0 && choice <= planet.length) {
                System.out.println("Planeta seleccionado: " + planet[choice - 1]);
            } else {
                System.err.println("Elección inválida. Por favor, elige un número entre 1 y " + planet.length);
                return selectPlanet(scanner); // Volver a llamar la función para una elección válida
            }
        } catch (InputMismatchException e) {
            System.err.println("Entrada inválida. Por favor, introduce un número.");
            scanner.next(); // Limpiar el buffer del scanner
            return selectPlanet(scanner); // Volver a llamar la función para una elección válida
        }
        return choice - 1;
    }

    public static int selectShip(Scanner scanner, int numberOfPassengers) {
        for (int i = 0; i < ships.length; i++) {
            System.out.println((i + 1) + ". " + ships[i]);
        }

        int choice = -1;
        try {
            choice = scanner.nextInt();
            if (choice > 0 && choice <= ships.length) {
                if (numberOfPassengers <= capacities[choice - 1]) {
                    System.out.println("Nave seleccionada: " + ships[choice - 1]);
                } else {
                    System.err.println("Con esta nave no se puede montar " + numberOfPassengers + " pasajeros.");
                    return selectShip(scanner, numberOfPassengers);
                }
            } else {
                System.err.println("Elección inválida. Por favor, elige un número entre 1 y " + ships.length);
                return selectShip(scanner, numberOfPassengers); // Volver a llamar la función para una elección válida
            }
        } catch (InputMismatchException e) {
            System.err.println("Entrada inválida. Por favor, introduce un número.");
            scanner.next(); // Limpiar el buffer del scanner
            return selectShip(scanner, numberOfPassengers); // Volver a llamar la función para una elección válida
        }
        return choice - 1;
    }

    public static double calculateTravelTime(double distance, double speed) {
        return distance / speed;
    }

    public static void startJourney(Scanner scanner, double distance, double speed, double initialFuelLevel, double oxygenLevel) {
        double travelTime = calculateTravelTime(distance, speed);
        double currentFuelLevel = initialFuelLevel;
        Random random = new Random();
        
        for (double day = 1; day <= travelTime; day++) {
            double progress = (day / travelTime) * 100;

            // Mostrar el progreso del viaje
            if (progress >= 20 && progress < 30) {
                System.out.println("20% del viaje completado.");
            } else if (progress >= 50 && progress < 60) {
                System.out.println("50% del viaje completado.");
            } else if (progress >= 80 && progress < 90) {
                System.out.println("80% del viaje completado.");
            }

            // Eventos aleatorios en puntos específicos
            if ((progress >= 20 && progress < 30) || (progress >= 50 && progress < 60) || (progress >= 80 && progress < 90)) {
                System.out.println("¡Asteroides detectados! Presiona 'E' para evadir.");
                String input = scanner.next();
                if (input.equalsIgnoreCase("E")) {
                    System.out.println("Has evadido los asteroides exitosamente.");
                } else {
                    System.out.println("Impacto con asteroides. La nave ha sufrido daños.");
                    currentFuelLevel -= 10; // Reducir combustible por daños
                }
            } else {
                System.out.println("Viaje sin incidentes.");
            }

            // Consumir combustible y oxígeno diario
            currentFuelLevel -= 5; // Consumo de combustible por día
            oxygenLevel -= 20; // Consumo de oxígeno por día
            if (currentFuelLevel <= 0) {
                System.out.println("Combustible agotado. Necesitas rellenar el combustible.");

                // Proporcionar al usuario la opción de rellenar el combustible
                boolean refilled = refillFuel(scanner);
                if (!refilled) {
                    System.out.println("No rellenaste el combustible a tiempo. La nave se ha apagado y estás divagando en el espacio.");
                    return;
                } else {
                    currentFuelLevel = 100.0; // Restablecer el nivel de combustible
                }
            }

            if (oxygenLevel <= 0) {
                System.out.println("Oxígeno agotado. El viaje no puede continuar.");
                return;
            }

            // Mostrar niveles de combustible y oxígeno
            System.out.printf("Nivel de combustible: %.2f%%%n", currentFuelLevel);
            System.out.printf("Nivel de oxígeno: %.2f litros%n", oxygenLevel);
        }

        System.out.println("¡Has llegado a tu destino!");
    }

    public static boolean refillFuel(Scanner scanner) {
        System.out.println("Presiona 'R' para rellenar el combustible.");
        String input = scanner.next();
    
        if (input.equalsIgnoreCase("R")) {
            System.out.println("Combustible rellenado.");
            return true;
        } else {
            System.out.println("No rellenaste el combustible a tiempo. La nave se ha apagado y estás divagando en el espacio.");
            return false;
        }
    }
}
    

