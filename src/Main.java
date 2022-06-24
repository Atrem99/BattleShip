import java.util.Scanner;

class Main {

    public static void main(String[] args) {

        Player1 player1 = new Player1();
        Player2 player2 = new Player2();

        player1.buildField();
        player1.printField();
        player1.printCondition();
        player1.buildFogField();

        player1.clickEnter();

        player2.buildField();
        player2.printField();
        player2.printCondition();
        player2.buildFogField();

        while (player1.counterDefeatP1 != 17 || player2.counterDefeatP2 != 17) {
            player1.clickEnter();

            player2.printFogField();
            System.out.println("---------------------");
            player1.printField();
            System.out.println("Player 1, it's your turn:");
            player2.takeShots();

            player2.clickEnter();

            player1.printFogField();
            System.out.println("---------------------");
            player2.printField();
            System.out.println("Player 2, it's your turn:");
            player1.takeShots();
        }
    }
}

class Battleship {
    private static final Scanner scanner = new Scanner(System.in);
    private static final char fog = '~';
    private static final char ship = 'O';
    private static final char miss = 'M';
    private static final char defeat = 'X';
    private static final int[] fieldNumbers = new int[11];
    private static final String fieldLetters = "ABCDEFGHIJ";
    private static final char[][] field = new char[10][11];
    private static final char[][] shootingField = new char[10][11];
    private static final String[] ships = new String[]{"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
    private static final int[] cells = new int[]{5, 4, 3, 3, 2};
    private static int userCells;
    private static int firstLetterCoordinate;
    private static int firstDigitCoordinate;
    private static int secondLetterCoordinate;
    private static int secondDigitCoordinate;
    private static int letterShots;
    private static int digitShots;
    private static int counterDefeat;


    public void clickEnter() {
        boolean flagEnter = false;
        while (!flagEnter) {
            System.out.println("Press Enter and pass the move to another player");
            String enter = scanner.nextLine();
            if (enter.isEmpty()) {
                flagEnter = true;
            }
        }
    }

    public void buildField() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = fog;
                field[i][0] = fieldLetters.charAt(i);
            }
        }
    }

    public void printField() {
        System.out.print("  ");
        for (int i = 1; i < fieldNumbers.length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (char[] chars : field) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    public void printCondition() {
        for (int i = 0; i < ships.length; i++) {
            System.out.printf("Enter the coordinates of the %s (%d cells): \n", ships[i], cells[i]);
            enterCoordinatesForShips();

            while (userCells != cells[i]) {
                System.out.printf("Error! Wrong length of the %s! Try again: \n", ships[i]);
                enterCoordinatesForShips();
            }
            if (userCells == cells[i]) {
                buildShips();
                printField();
            }
        }
    }


    public void enterCoordinatesForShips() {
        String[] coordinatesShips = scanner.nextLine().split(" ");
        char[] coordinate1Chars = coordinatesShips[0].toCharArray();
        char[] coordinate2Chars = coordinatesShips[1].toCharArray();

        firstLetterCoordinate = coordinate1Chars[0] - 65;
        firstDigitCoordinate = coordinate1Chars[1] - 48;
        secondLetterCoordinate = coordinate2Chars[0] - 65;
        secondDigitCoordinate = coordinate2Chars[1] - 48;

        if (coordinate1Chars.length > 2) {
            firstDigitCoordinate = 10;
        }
        if (coordinate2Chars.length > 2) {
            secondDigitCoordinate = 10;
        }

        for (char[] chars : field) {
            for (int j = 0; j < chars.length; j++) {
                if ((firstLetterCoordinate - secondLetterCoordinate) == 0) {
                    if (firstDigitCoordinate > secondDigitCoordinate) {
                        userCells = (firstDigitCoordinate - secondDigitCoordinate) + 1;
                    } else if (firstDigitCoordinate < secondDigitCoordinate) {
                        userCells = (secondDigitCoordinate - firstDigitCoordinate) + 1;
                    }
                } else if ((firstDigitCoordinate - secondDigitCoordinate) == 0) {
                    if (firstLetterCoordinate > secondLetterCoordinate) {
                        userCells = (firstLetterCoordinate - secondLetterCoordinate) + 1;
                    } else {
                        userCells = (secondLetterCoordinate - firstLetterCoordinate) + 1;
                    }
                } else {
                    System.out.println("Error! Wrong ship location! Try again:");
                    enterCoordinatesForShips();
                }
            }
        }

        try {
            if (field[firstLetterCoordinate][firstDigitCoordinate - 1] == ship ||
                    field[secondLetterCoordinate][secondDigitCoordinate - 1] == ship ||

                    field[firstLetterCoordinate][firstDigitCoordinate + 1] == ship ||
                    field[secondLetterCoordinate][secondDigitCoordinate + 1] == ship ||

                    field[firstLetterCoordinate + 1][firstDigitCoordinate] == ship ||
                    field[secondLetterCoordinate + 1][secondDigitCoordinate] == ship ||

                    field[firstLetterCoordinate - 1][firstDigitCoordinate] == ship ||
                    field[secondLetterCoordinate - 1][secondDigitCoordinate] == ship ||

                    field[firstLetterCoordinate - 1][firstDigitCoordinate - 1] == ship ||
                    field[secondLetterCoordinate - 1][secondDigitCoordinate - 1] == ship ||

                    field[firstLetterCoordinate + 1][firstDigitCoordinate + 1] == ship ||
                    field[secondLetterCoordinate + 1][secondDigitCoordinate + 1] == ship ||

                    field[firstLetterCoordinate - 1][firstDigitCoordinate + 1] == ship ||
                    field[secondLetterCoordinate - 1][secondDigitCoordinate + 1] == ship ||

                    field[firstLetterCoordinate + 1][firstDigitCoordinate - 1] == ship ||
                    field[secondLetterCoordinate + 1][secondDigitCoordinate - 1] == ship) {
                System.out.println("Error! You placed it too close to another one. Try again:");
                enterCoordinatesForShips();
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public void buildShips() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if ((firstLetterCoordinate - secondLetterCoordinate) == 0) {
                    if (firstDigitCoordinate > secondDigitCoordinate) {
                        while (firstDigitCoordinate >= secondDigitCoordinate) {
                            field[firstLetterCoordinate][firstDigitCoordinate] = ship;
                            firstDigitCoordinate -= 1;
                        }
                        firstDigitCoordinate += userCells;

                    } else if (firstDigitCoordinate < secondDigitCoordinate) {
                        while (secondDigitCoordinate >= firstDigitCoordinate) {
                            field[secondLetterCoordinate][secondDigitCoordinate] = ship;
                            secondDigitCoordinate -= 1;
                        }
                        secondDigitCoordinate += userCells;
                    }
                } else if ((firstDigitCoordinate - secondDigitCoordinate) == 0) {
                    if (firstLetterCoordinate > secondLetterCoordinate) {
                        while (firstLetterCoordinate >= secondLetterCoordinate) {
                            field[firstLetterCoordinate][firstDigitCoordinate] = ship;
                            firstLetterCoordinate -= 1;
                        }
                        firstLetterCoordinate += userCells;
                    } else {
                        while (firstLetterCoordinate <= secondLetterCoordinate) {
                            field[secondLetterCoordinate][secondDigitCoordinate] = ship;
                            secondLetterCoordinate -= 1;
                        }
                        secondLetterCoordinate += userCells;
                    }
                }
            }
        }
    }

    public void takeShots() {
        enterCoordinatesForShots();
        setShot();
        if (field[letterShots][digitShots] == defeat) {
            counterDefeat++;
            if (counterDefeat == 17) {
                System.out.println("You sank the last ship. You won. Congratulations!");

            } else {
                try {
                    if (field[letterShots][digitShots - 1] != ship &&
                            field[letterShots][digitShots + 1] != ship &&
                            field[letterShots + 1][digitShots] != ship &&
                            field[letterShots - 1][digitShots] != ship &&
                            field[letterShots][digitShots] == defeat) {
                        System.out.println("You sank a ship! Specify a new target: ");
                    } else {
                        System.out.println("You hit a ship! Try again: ");
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    System.out.println("You sank a ship! Specify a new target: ");
                }
            }
        } else if (field[letterShots][digitShots] == miss) {
            System.out.println("You missed. Try again: ");
        }
    }

    public void buildFogField() {
        for (int i = 0; i < shootingField.length; i++) {
            for (int j = 0; j < shootingField[i].length; j++) {
                shootingField[i][j] = fog;
                shootingField[i][0] = fieldLetters.charAt(i);
            }
        }
    }

    public void printFogField() {
        buildFogField();
        System.out.print("  ");
        for (int i = 1; i < fieldNumbers.length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (char[] chars : shootingField) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    public void enterCoordinatesForShots() {
        String coordinatesShots = scanner.nextLine();
        letterShots = coordinatesShots.charAt(0) - 65;
        digitShots = coordinatesShots.charAt(1) - 48;

        if (coordinatesShots.length() > 2) {
            digitShots = 10;
        }

        if (digitShots > 10 || letterShots > 10) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            enterCoordinatesForShots();
        }
    }

    public void setShot() {
        if (field[letterShots][digitShots] == ship) {
            shootingField[letterShots][digitShots] = defeat;
            field[letterShots][digitShots] = defeat;
        } else if (field[letterShots][digitShots] == fog) {
            shootingField[letterShots][digitShots] = miss;
            field[letterShots][digitShots] = miss;
        } else if (field[letterShots][digitShots] == defeat) {
            counterDefeat--;
            System.out.println("You hit a ship! Try again: ");
        }
    }
}

class Player1 extends Battleship {
    private final Scanner scannerP1 = new Scanner(System.in);
    private final char fogP1 = '~';
    private final char shipP1 = 'O';
    private final char missP1 = 'M';
    private final char defeatP1 = 'X';
    private final int[] fieldNumbersP1 = new int[11];
    private final String fieldLettersP1 = "ABCDEFGHIJ";
    private final char[][] fieldP1 = new char[10][11];
    private final char[][] shootingFieldP1 = new char[10][11];
    private final String[] shipsP1 = new String[]{"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
    private final int[] cellsP1 = new int[]{5, 4, 3, 3, 2};
    private int userCellsP1;
    private int firstLetterCoordinateP1;
    private int firstDigitCoordinateP1;
    private int secondLetterCoordinateP1;
    private int secondDigitCoordinateP1;
    private int letterShotsP1;
    private int digitShotsP1;
    public int counterDefeatP1;

    @Override
    public void buildField() {
        System.out.println("Player 1, place your ships to the game field");
        for (int i = 0; i < fieldP1.length; i++) {
            for (int j = 0; j < fieldP1[i].length; j++) {
                fieldP1[i][j] = fogP1;
                fieldP1[i][0] = fieldLettersP1.charAt(i);
            }
        }
    }

    @Override
    public void printField() {
        System.out.print("  ");
        for (int i = 1; i < fieldNumbersP1.length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (char[] chars : fieldP1) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void printCondition() {
        for (int i = 0; i < shipsP1.length; i++) {
            System.out.printf("Enter the coordinates of the %s (%d cells): \n", shipsP1[i], cellsP1[i]);
            enterCoordinatesForShips();

            while (userCellsP1 != cellsP1[i]) {
                System.out.printf("Error! Wrong length of the %s! Try again: \n", shipsP1[i]);
                enterCoordinatesForShips();
            }
            if (userCellsP1 == cellsP1[i]) {
                buildShips();
                printField();
            }
        }
    }

    @Override
    public void enterCoordinatesForShips() {
        String[] coordinatesShips = scannerP1.nextLine().split(" ");
        char[] coordinate1Chars = coordinatesShips[0].toCharArray();
        char[] coordinate2Chars = coordinatesShips[1].toCharArray();

        firstLetterCoordinateP1 = coordinate1Chars[0] - 65;
        firstDigitCoordinateP1 = coordinate1Chars[1] - 48;
        secondLetterCoordinateP1 = coordinate2Chars[0] - 65;
        secondDigitCoordinateP1 = coordinate2Chars[1] - 48;

        if (coordinate1Chars.length > 2) {
            firstDigitCoordinateP1 = 10;
        }
        if (coordinate2Chars.length > 2) {
            secondDigitCoordinateP1 = 10;
        }

        for (char[] chars : fieldP1) {
            for (int j = 0; j < chars.length; j++) {
                if ((firstLetterCoordinateP1 - secondLetterCoordinateP1) == 0) {
                    if (firstDigitCoordinateP1 > secondDigitCoordinateP1) {
                        userCellsP1 = (firstDigitCoordinateP1 - secondDigitCoordinateP1) + 1;
                    } else if (firstDigitCoordinateP1 < secondDigitCoordinateP1) {
                        userCellsP1 = (secondDigitCoordinateP1 - firstDigitCoordinateP1) + 1;
                    }
                } else if ((firstDigitCoordinateP1 - secondDigitCoordinateP1) == 0) {
                    if (firstLetterCoordinateP1 > secondLetterCoordinateP1) {
                        userCellsP1 = (firstLetterCoordinateP1 - secondLetterCoordinateP1) + 1;
                    } else {
                        userCellsP1 = (secondLetterCoordinateP1 - firstLetterCoordinateP1) + 1;
                    }
                } else {
                    System.out.println("Error! Wrong ship location! Try again:");
                    enterCoordinatesForShips();
                }
            }
        }

        try {
            if (fieldP1[firstLetterCoordinateP1][firstDigitCoordinateP1 - 1] == shipP1 ||
                    fieldP1[secondLetterCoordinateP1][secondDigitCoordinateP1 - 1] == shipP1 ||

                    fieldP1[firstLetterCoordinateP1][firstDigitCoordinateP1 + 1] == shipP1 ||
                    fieldP1[secondLetterCoordinateP1][secondDigitCoordinateP1 + 1] == shipP1 ||

                    fieldP1[firstLetterCoordinateP1 + 1][firstDigitCoordinateP1] == shipP1 ||
                    fieldP1[secondLetterCoordinateP1 + 1][secondDigitCoordinateP1] == shipP1 ||

                    fieldP1[firstLetterCoordinateP1 - 1][firstDigitCoordinateP1] == shipP1 ||
                    fieldP1[secondLetterCoordinateP1 - 1][secondDigitCoordinateP1] == shipP1 ||

                    fieldP1[firstLetterCoordinateP1 - 1][firstDigitCoordinateP1 - 1] == shipP1 ||
                    fieldP1[secondLetterCoordinateP1 - 1][secondDigitCoordinateP1 - 1] == shipP1 ||

                    fieldP1[firstLetterCoordinateP1 + 1][firstDigitCoordinateP1 + 1] == shipP1 ||
                    fieldP1[secondLetterCoordinateP1 + 1][secondDigitCoordinateP1 + 1] == shipP1 ||

                    fieldP1[firstLetterCoordinateP1 - 1][firstDigitCoordinateP1 + 1] == shipP1 ||
                    fieldP1[secondLetterCoordinateP1 - 1][secondDigitCoordinateP1 + 1] == shipP1 ||

                    fieldP1[firstLetterCoordinateP1 + 1][firstDigitCoordinateP1 - 1] == shipP1 ||
                    fieldP1[secondLetterCoordinateP1 + 1][secondDigitCoordinateP1 - 1] == shipP1) {
                System.out.println("Error! You placed it too close to another one. Try again:");
                enterCoordinatesForShips();
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public void buildShips() {
        for (int i = 0; i < fieldP1.length; i++) {
            for (int j = 0; j < fieldP1[i].length; j++) {
                if ((firstLetterCoordinateP1 - secondLetterCoordinateP1) == 0) {
                    if (firstDigitCoordinateP1 > secondDigitCoordinateP1) {
                        while (firstDigitCoordinateP1 >= secondDigitCoordinateP1) {
                            fieldP1[firstLetterCoordinateP1][firstDigitCoordinateP1] = shipP1;
                            firstDigitCoordinateP1 -= 1;
                        }
                        firstDigitCoordinateP1 += userCellsP1;

                    } else if (firstDigitCoordinateP1 < secondDigitCoordinateP1) {
                        while (secondDigitCoordinateP1 >= firstDigitCoordinateP1) {
                            fieldP1[secondLetterCoordinateP1][secondDigitCoordinateP1] = shipP1;
                            secondDigitCoordinateP1 -= 1;
                        }
                        secondDigitCoordinateP1 += userCellsP1;
                    }
                } else if ((firstDigitCoordinateP1 - secondDigitCoordinateP1) == 0) {
                    if (firstLetterCoordinateP1 > secondLetterCoordinateP1) {
                        while (firstLetterCoordinateP1 >= secondLetterCoordinateP1) {
                            fieldP1[firstLetterCoordinateP1][firstDigitCoordinateP1] = shipP1;
                            firstLetterCoordinateP1 -= 1;
                        }
                        firstLetterCoordinateP1 += userCellsP1;
                    } else {
                        while (firstLetterCoordinateP1 <= secondLetterCoordinateP1) {
                            fieldP1[secondLetterCoordinateP1][secondDigitCoordinateP1] = shipP1;
                            secondLetterCoordinateP1 -= 1;
                        }
                        secondLetterCoordinateP1 += userCellsP1;
                    }
                }
            }
        }
    }


    @Override
    public void takeShots() {
        enterCoordinatesForShots();
        setShot();
        if (fieldP1[letterShotsP1][digitShotsP1] == defeatP1) {
            counterDefeatP1++;
            if (counterDefeatP1 == 17) {
                System.out.println("You sank the last ship. You won. Congratulations!");
            } else {
                try {
                    if (fieldP1[letterShotsP1][digitShotsP1 - 1] != shipP1 &&
                            fieldP1[letterShotsP1][digitShotsP1 + 1] != shipP1 &&
                            fieldP1[letterShotsP1 + 1][digitShotsP1] != shipP1 &&
                            fieldP1[letterShotsP1 - 1][digitShotsP1] != shipP1 &&
                            fieldP1[letterShotsP1][digitShotsP1] == defeatP1) {
                        System.out.println("You sank a ship! Specify a new target: ");
                    } else {
                        System.out.println("You hit a ship! Try again: ");
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    System.out.println("You sank a ship! Specify a new target: ");
                }
            }
        } else if (fieldP1[letterShotsP1][digitShotsP1] == missP1) {
            System.out.println("You missed. Try again: ");
        }
    }

    @Override
    public void buildFogField() {
        for (int i = 0; i < shootingFieldP1.length; i++) {
            for (int j = 0; j < shootingFieldP1[i].length; j++) {
                shootingFieldP1[i][j] = fogP1;
                shootingFieldP1[i][0] = fieldLettersP1.charAt(i);
            }
        }
    }

    @Override
    public void printFogField() {
        System.out.print("  ");
        for (int i = 1; i < fieldNumbersP1.length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (char[] chars : shootingFieldP1) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void enterCoordinatesForShots() {
        String coordinatesShots = scannerP1.nextLine();
        letterShotsP1 = coordinatesShots.charAt(0) - 65;
        digitShotsP1 = coordinatesShots.charAt(1) - 48;

        if (coordinatesShots.length() > 2) {
            digitShotsP1 = 10;
        }

        if (digitShotsP1 > 10 || letterShotsP1 > 10) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            enterCoordinatesForShots();
        }
    }

    @Override
    public void setShot() {
        if (fieldP1[letterShotsP1][digitShotsP1] == shipP1) {
            shootingFieldP1[letterShotsP1][digitShotsP1] = defeatP1;
            fieldP1[letterShotsP1][digitShotsP1] = defeatP1;
            shootingFieldP1[letterShotsP1][digitShotsP1] = defeatP1;
        } else if (fieldP1[letterShotsP1][digitShotsP1] == fogP1) {
            shootingFieldP1[letterShotsP1][digitShotsP1] = missP1;
            fieldP1[letterShotsP1][digitShotsP1] = missP1;
            shootingFieldP1[letterShotsP1][digitShotsP1] = missP1;
        } else if (fieldP1[letterShotsP1][digitShotsP1] == defeatP1) {
            counterDefeatP1--;
            System.out.println("You hit a ship! Try again: ");
        }
    }

    @Override
    public void clickEnter() {
        super.clickEnter();
    }
}

class Player2 extends Battleship {
    private static final Scanner scannerP2 = new Scanner(System.in);
    private static final char fogP2 = '~';
    private static final char shipP2 = 'O';
    private static final char missP2 = 'M';
    private static final char defeatP2 = 'X';
    private static final int[] fieldNumbersP2 = new int[11];
    private static final String fieldLettersP2 = "ABCDEFGHIJ";
    private static final char[][] fieldP2 = new char[10][11];
    private static final char[][] shootingFieldP2 = new char[10][11];
    private static final String[] shipsP2 = new String[]{"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
    private static final int[] cellsP2 = new int[]{5, 4, 3, 3, 2};
    private static int userCellsP2;
    private static int firstLetterCoordinateP2;
    private static int firstDigitCoordinateP2;
    private static int secondLetterCoordinateP2;
    private static int secondDigitCoordinateP2;
    private static int letterShotsP2;
    private static int digitShotsP2;
    public int counterDefeatP2;

    @Override
    public void buildField() {
        System.out.println("Player 2, place your ships to the game field");
        for (int i = 0; i < fieldP2.length; i++) {
            for (int j = 0; j < fieldP2[i].length; j++) {
                fieldP2[i][j] = fogP2;
                fieldP2[i][0] = fieldLettersP2.charAt(i);
            }
        }
    }

    @Override
    public void printField() {
        System.out.print("  ");
        for (int i = 1; i < fieldNumbersP2.length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (char[] chars : fieldP2) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void printCondition() {
        for (int i = 0; i < shipsP2.length; i++) {
            System.out.printf("Enter the coordinates of the %s (%d cells): \n", shipsP2[i], cellsP2[i]);
            enterCoordinatesForShips();

            while (userCellsP2 != cellsP2[i]) {
                System.out.printf("Error! Wrong length of the %s! Try again: \n", shipsP2[i]);
                enterCoordinatesForShips();
            }
            if (userCellsP2 == cellsP2[i]) {
                buildShips();
                printField();
            }
        }
    }

    @Override
    public void enterCoordinatesForShips() {
        String[] coordinatesShips = scannerP2.nextLine().split(" ");
        char[] coordinate1Chars = coordinatesShips[0].toCharArray();
        char[] coordinate2Chars = coordinatesShips[1].toCharArray();

        firstLetterCoordinateP2 = coordinate1Chars[0] - 65;
        firstDigitCoordinateP2 = coordinate1Chars[1] - 48;
        secondLetterCoordinateP2 = coordinate2Chars[0] - 65;
        secondDigitCoordinateP2 = coordinate2Chars[1] - 48;

        if (coordinate1Chars.length > 2) {
            firstDigitCoordinateP2 = 10;
        }
        if (coordinate2Chars.length > 2) {
            secondDigitCoordinateP2 = 10;
        }

        for (char[] chars : fieldP2) {
            for (int j = 0; j < chars.length; j++) {
                if ((firstLetterCoordinateP2 - secondLetterCoordinateP2) == 0) {
                    if (firstDigitCoordinateP2 > secondDigitCoordinateP2) {
                        userCellsP2 = (firstDigitCoordinateP2 - secondDigitCoordinateP2) + 1;
                    } else if (firstDigitCoordinateP2 < secondDigitCoordinateP2) {
                        userCellsP2 = (secondDigitCoordinateP2 - firstDigitCoordinateP2) + 1;
                    }
                } else if ((firstDigitCoordinateP2 - secondDigitCoordinateP2) == 0) {
                    if (firstLetterCoordinateP2 > secondLetterCoordinateP2) {
                        userCellsP2 = (firstLetterCoordinateP2 - secondLetterCoordinateP2) + 1;
                    } else {
                        userCellsP2 = (secondLetterCoordinateP2 - firstLetterCoordinateP2) + 1;
                    }
                } else {
                    System.out.println("Error! Wrong ship location! Try again:");
                    enterCoordinatesForShips();
                }
            }
        }

        try {
            if (fieldP2[firstLetterCoordinateP2][firstDigitCoordinateP2 - 1] == shipP2 ||
                    fieldP2[secondLetterCoordinateP2][secondDigitCoordinateP2 - 1] == shipP2 ||

                    fieldP2[firstLetterCoordinateP2][firstDigitCoordinateP2 + 1] == shipP2 ||
                    fieldP2[secondLetterCoordinateP2][secondDigitCoordinateP2 + 1] == shipP2 ||

                    fieldP2[firstLetterCoordinateP2 + 1][firstDigitCoordinateP2] == shipP2 ||
                    fieldP2[secondLetterCoordinateP2 + 1][secondDigitCoordinateP2] == shipP2 ||

                    fieldP2[firstLetterCoordinateP2 - 1][firstDigitCoordinateP2] == shipP2 ||
                    fieldP2[secondLetterCoordinateP2 - 1][secondDigitCoordinateP2] == shipP2 ||

                    fieldP2[firstLetterCoordinateP2 - 1][firstDigitCoordinateP2 - 1] == shipP2 ||
                    fieldP2[secondLetterCoordinateP2 - 1][secondDigitCoordinateP2 - 1] == shipP2 ||

                    fieldP2[firstLetterCoordinateP2 + 1][firstDigitCoordinateP2 + 1] == shipP2 ||
                    fieldP2[secondLetterCoordinateP2 + 1][secondDigitCoordinateP2 + 1] == shipP2 ||

                    fieldP2[firstLetterCoordinateP2 - 1][firstDigitCoordinateP2 + 1] == shipP2 ||
                    fieldP2[secondLetterCoordinateP2 - 1][secondDigitCoordinateP2 + 1] == shipP2 ||

                    fieldP2[firstLetterCoordinateP2 + 1][firstDigitCoordinateP2 - 1] == shipP2 ||
                    fieldP2[secondLetterCoordinateP2 + 1][secondDigitCoordinateP2 - 1] == shipP2) {
                System.out.println("Error! You placed it too close to another one. Try again:");
                enterCoordinatesForShips();
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    @Override
    public void buildShips() {
        for (int i = 0; i < fieldP2.length; i++) {
            for (int j = 0; j < fieldP2[i].length; j++) {
                if ((firstLetterCoordinateP2 - secondLetterCoordinateP2) == 0) {
                    if (firstDigitCoordinateP2 > secondDigitCoordinateP2) {
                        while (firstDigitCoordinateP2 >= secondDigitCoordinateP2) {
                            fieldP2[firstLetterCoordinateP2][firstDigitCoordinateP2] = shipP2;
                            firstDigitCoordinateP2 -= 1;
                        }
                        firstDigitCoordinateP2 += userCellsP2;

                    } else if (firstDigitCoordinateP2 < secondDigitCoordinateP2) {
                        while (secondDigitCoordinateP2 >= firstDigitCoordinateP2) {
                            fieldP2[secondLetterCoordinateP2][secondDigitCoordinateP2] = shipP2;
                            secondDigitCoordinateP2 -= 1;
                        }
                        secondDigitCoordinateP2 += userCellsP2;
                    }
                } else if ((firstDigitCoordinateP2 - secondDigitCoordinateP2) == 0) {
                    if (firstLetterCoordinateP2 > secondLetterCoordinateP2) {
                        while (firstLetterCoordinateP2 >= secondLetterCoordinateP2) {
                            fieldP2[firstLetterCoordinateP2][firstDigitCoordinateP2] = shipP2;
                            firstLetterCoordinateP2 -= 1;
                        }
                        firstLetterCoordinateP2 += userCellsP2;
                    } else {
                        while (firstLetterCoordinateP2 <= secondLetterCoordinateP2) {
                            fieldP2[secondLetterCoordinateP2][secondDigitCoordinateP2] = shipP2;
                            secondLetterCoordinateP2 -= 1;
                        }
                        secondLetterCoordinateP2 += userCellsP2;
                    }
                }
            }
        }
    }


    @Override
    public void takeShots() {
        enterCoordinatesForShots();
        setShot();
        if (fieldP2[letterShotsP2][digitShotsP2] == defeatP2) {
            counterDefeatP2++;
            if (counterDefeatP2 == 17) {
                System.out.println("You sank the last ship. You won. Congratulations!");
            } else {
                try {
                    if (fieldP2[letterShotsP2][digitShotsP2 - 1] != shipP2 &&
                            fieldP2[letterShotsP2][digitShotsP2 + 1] != shipP2 &&
                            fieldP2[letterShotsP2 + 1][digitShotsP2] != shipP2 &&
                            fieldP2[letterShotsP2 - 1][digitShotsP2] != shipP2 &&
                            fieldP2[letterShotsP2][digitShotsP2] == defeatP2) {
                        System.out.println("You sank a ship! Specify a new target: ");
                    } else {
                        System.out.println("You hit a ship! Try again: ");
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    System.out.println("You sank a ship! Specify a new target: ");
                }
            }
        } else if (fieldP2[letterShotsP2][digitShotsP2] == missP2) {
            System.out.println("You missed. Try again: ");
        }
    }

    @Override
    public void buildFogField() {
        for (int i = 0; i < shootingFieldP2.length; i++) {
            for (int j = 0; j < shootingFieldP2[i].length; j++) {
                shootingFieldP2[i][j] = fogP2;
                shootingFieldP2[i][0] = fieldLettersP2.charAt(i);
            }
        }
    }

    @Override
    public void printFogField() {
        System.out.print("  ");
        for (int i = 1; i < fieldNumbersP2.length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (char[] chars : shootingFieldP2) {
            for (char aChar : chars) {
                System.out.print(aChar + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void enterCoordinatesForShots() {
        String coordinatesShots = scannerP2.nextLine();
        letterShotsP2 = coordinatesShots.charAt(0) - 65;
        digitShotsP2 = coordinatesShots.charAt(1) - 48;

        if (coordinatesShots.length() > 2) {
            digitShotsP2 = 10;
        }

        if (digitShotsP2 > 10 || letterShotsP2 > 10) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            enterCoordinatesForShots();
        }
    }

    @Override
    public void setShot() {
        if (fieldP2[letterShotsP2][digitShotsP2] == shipP2) {
            shootingFieldP2[letterShotsP2][digitShotsP2] = defeatP2;
            fieldP2[letterShotsP2][digitShotsP2] = defeatP2;
            shootingFieldP2[letterShotsP2][digitShotsP2] = defeatP2;
        } else if (fieldP2[letterShotsP2][digitShotsP2] == fogP2) {
            shootingFieldP2[letterShotsP2][digitShotsP2] = missP2;
            fieldP2[letterShotsP2][digitShotsP2] = missP2;
            shootingFieldP2[letterShotsP2][digitShotsP2] = missP2;
        } else if (fieldP2[letterShotsP2][digitShotsP2] == defeatP2) {
            counterDefeatP2--;
            System.out.println("You hit a ship! Try again: ");
        }
    }
}