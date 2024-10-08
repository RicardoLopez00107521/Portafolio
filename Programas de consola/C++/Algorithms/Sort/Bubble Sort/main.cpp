#include <iostream>

using namespace std;

int arr[20] = {34, 12, 7, 23, 45, 9, 78, 56, 3, 19, 88, 14, 67, 2, 41, 5, 90, 32, 11, 54};

void swap(int&, int&);
void print_array();
void bubble_sort();

int main() {

    cout << "Original array" << endl;
    print_array();

    bubble_sort();

    cout << endl << "Sorted array" << endl;
    print_array();

    return 0;
}

void swap(int& a, int& b) {
    int temp = a;
    a = b;
    b = temp;
}

void print_array() {
    for (int i = 0; i < 20; ++i) {
        cout << arr [i] << " ";
    }
    cout << endl;
}

void bubble_sort() {
    for (int j = 0; j < 20 - 1; j++) {
        for (int i = 0; i < 20 - j - 1; i++) {
            if (arr[i] > arr[i+1]) {
                swap(arr[i], arr[i + 1]);
            }  
        }
    }
}