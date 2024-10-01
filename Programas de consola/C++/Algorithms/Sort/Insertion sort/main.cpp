#include <iostream>

using namespace std;

int arr[20] = {34, 12, 7, 23, 45, 9, 78, 56, 3, 19, 88, 14, 67, 2, 41, 5, 90, 32, 11, 54};

void insertion_sort();
void print_array();

int main() {

    cout << "Original array" << endl;
    print_array();

    insertion_sort();

    cout << "Sorted array" << endl;
    print_array();
    return 0;
}

void print_array() {
    for (int i = 0; i < 20; i++) {
        cout << arr[i] << " ";
    }
    cout << endl;
}

void insertion_sort() {
    for (int j = 0; j < 20; j++) {
        int key = arr[j];
        int i = j - 1;

        while (i >= 0 && arr[i] > key) {
            arr[i + 1] = arr[i];
            i--;
        }
        arr[i + 1] = key;
    }
    
}