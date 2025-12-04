import java.util.Arrays;
import java.util.Random;

public class SortingAnalysic {

    //Сортировка вставками
    public static void insertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int current = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > current) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = current;
        }
    }

    //ортировка слиянием
    public static void mergeSort(int[] array) {
        if (array.length <= 1) return;
        int middle = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, middle);
        int[] right = Arrays.copyOfRange(array, middle, array.length);
        mergeSort(left);
        mergeSort(right);
        merge(array, left, right);
    }

    private static void merge(int[] array, int[] left, int[] right) {
        int leftIndex = 0, rightIndex = 0, arrayIndex = 0;
        while (leftIndex < left.length && rightIndex < right.length) {
            if (left[leftIndex] <= right[rightIndex]) {
                array[arrayIndex++] = left[leftIndex++];
            } else {
                array[arrayIndex++] = right[rightIndex++];
            }
        }
        while (leftIndex < left.length) array[arrayIndex++] = left[leftIndex++];
        while (rightIndex < right.length) array[arrayIndex++] = right[rightIndex++];
    }

    //Сортировка расчёской
    public static void combSort(int[] array) {
        int gap = array.length;
        boolean swapped = true;
        double shrink = 1.3;

        while (gap > 1 || swapped) {
            gap = Math.max(1, (int)(gap / shrink));
            swapped = false;

            for (int i = 0; i + gap < array.length; i++) {
                if (array[i] > array[i + gap]) {
                    int temp = array[i];
                    array[i] = array[i + gap];
                    array[i + gap] = temp;
                    swapped = true;
                }
            }
        }
    }

    //Генерация массива
    public static int[] createRandomArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) array[i] = random.nextInt(1000);
        return array;
    }

    public static int[] createPartiallySortedArray(int size, double sortedPercentage) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) array[i] = i * 10;
        int elementsToShuffle = (int)(size * (1 - sortedPercentage));
        for (int i = 0; i < elementsToShuffle; i++) {
            int index1 = random.nextInt(size);
            int index2 = random.nextInt(size);
            int temp = array[index1];
            array[index1] = array[index2];
            array[index2] = temp;
        }
        return array;
    }

    public static int[] createReverseSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) array[i] = (size - i) * 10;
        return array;
    }

    public static int[] createArrayWithDuplicates(int size, double uniquePercentage) {
        int[] array = new int[size];
        Random random = new Random();
        int uniqueCount = Math.max(1, (int)(size * uniquePercentage));
        int[] uniqueValues = new int[uniqueCount];
        for (int i = 0; i < uniqueCount; i++) uniqueValues[i] = random.nextInt(100);
        for (int i = 0; i < size; i++) array[i] = uniqueValues[random.nextInt(uniqueCount)];
        return array;
    }

    public static int[] createAlmostSortedArray(int size, double sortedPercentage) {
        return createPartiallySortedArray(size, sortedPercentage);
    }

    //Тест
    public static void runPerformanceTests() {
        int[] arraySizes = {50, 250, 1000};
        String[] sizeDescriptions = {"Маленький (50 элементов)", "Средний (250 элементов)", "Большой (1000 элементов)"};

        String[] testTypes = {
                "Случайный массив",
                "Частично отсортированный (75% элементов на своих местах)",
                "Обратно отсортированный",
                "Много дубликатов (10% уникальных значений)",
                "Почти отсортированный (90% элементов на своих местах)"
        };

        String[] algorithmNames = {"Сортировка вставками", "Сортировка слиянием", "Сортировка расчёской"};

        System.out.println("СРАВНЕНИЕ ПРОИЗВОДИТЕЛЬНОСТИ АЛГОРИТМОВ СОРТИРОВКИ\n");

        for (int sizeIndex = 0; sizeIndex < arraySizes.length; sizeIndex++) {
            int currentSize = arraySizes[sizeIndex];
            System.out.println("\n" + sizeDescriptions[sizeIndex] + ":");
            System.out.println("=".repeat(50));


            int[] randomArray = createRandomArray(currentSize);
            int[] partiallySorted = createPartiallySortedArray(currentSize, 0.75);
            int[] reverseSorted = createReverseSortedArray(currentSize);
            int[] duplicatesArray = createArrayWithDuplicates(currentSize, 0.1);
            int[] almostSorted = createAlmostSortedArray(currentSize, 0.9);

            int[][][] testArrays = {
                    {randomArray.clone(), randomArray.clone(), randomArray.clone()},
                    {partiallySorted.clone(), partiallySorted.clone(), partiallySorted.clone()},
                    {reverseSorted.clone(), reverseSorted.clone(), reverseSorted.clone()},
                    {duplicatesArray.clone(), duplicatesArray.clone(), duplicatesArray.clone()},
                    {almostSorted.clone(), almostSorted.clone(), almostSorted.clone()}
            };

            //Тестирование для каждого типа массива
            for (int testIndex = 0; testIndex < testTypes.length; testIndex++) {
                System.out.println("\n" + testTypes[testIndex] + ":");


                for (int algorithmIndex = 0; algorithmIndex < 3; algorithmIndex++) {
                    long totalTime = 0;
                    long bestTime = Long.MAX_VALUE;
                    int numberOfRuns = 5;

                    for (int run = 0; run < numberOfRuns; run++) {
                        int[] arrayCopy = testArrays[testIndex][algorithmIndex].clone();
                        long startTime = System.nanoTime();

                        switch (algorithmIndex) {
                            case 0: insertionSort(arrayCopy); break;
                            case 1: mergeSort(arrayCopy); break;
                            case 2: combSort(arrayCopy); break;
                        }

                        long endTime = System.nanoTime();
                        long executionTime = endTime - startTime;
                        totalTime += executionTime;
                        if (executionTime < bestTime) {
                            bestTime = executionTime;
                        }
                    }

                    long averageTime = totalTime / numberOfRuns;
                    System.out.printf("  %-25s: Среднее %d нс, Лучшее %d нс%n",
                            algorithmNames[algorithmIndex], averageTime, bestTime);
                }
            }
        }
    }

    public static void main(String[] args) {
        runPerformanceTests();
    }
}
