#include <iostream>

using namespace std;

int arr[20] = {34, 12, 7, 23, 45, 9, 78, 56, 3, 19, 88, 14, 67, 2, 41, 5, 90, 32, 11, 54};

void swap(int&, int&);
void insertion_sort();
void print_array();

int main() {

    cout << "Original array" << endl;
    print_array();

    cout << "Sorted array" << endl;
    print_array();

    return 0;
}

void swap(int& a, int& b) {
    int temp = a;
    a = b;
    b = temp;
}