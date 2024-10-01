#include <iostream>

using namespace std;

int arr[20] = {34, 12, 7, 23, 45, 9, 78, 56, 3, 19, 88, 14, 67, 2, 41, 5, 90, 32, 11, 54};

void swap(int&, int&);
void selection_sort();
void print_array();

int main() {

    cout << "Original array" << endl;
    print_array();

    selection_sort();

    cout << "Sorted array" << endl;
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
        cout << arr[i] << " ";
    }
    cout << endl;
}

void selection_sort() {
    for (int j = 0; j < 20 - 1 ; ++j) {
        int min_index = j;

        for (int i = j + 1; i < 20; ++i) {
            if (arr[i] < arr[min_index]) 
                min_index = i;
        }
        swap(arr[j], arr[min_index]);
    }
}