#include <iostream>

using namespace std;

int array[20] = {34, 12, 7, 23, 45, 9, 78, 56, 3, 19, 88, 14, 67, 2, 41, 5, 90, 32, 11, 54};

void swap(int&, int&);
void print_array();
void bubble_sort(int&);

int main() {

    cout << "Original array" << endl;
    print_array();

    /*cout << endl << "Sorted array" << endl;
    print_array();*/

    return 0;
}

void swap(int& a, int& b) {
    int temp = a;
    a = b;
    b = temp;
}

void print_array() {
    for (int i = 0; i < 20; ++i) {
        cout << array [i] << " ";
    }
    cout << endl;
}