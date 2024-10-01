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