#include <iostream>

using namespace std; 

void merge_sort(int&, int l, int mid, int r);
void merge();
void print_array(); 

int main() {
    int arr[20] = {34, 12, 7, 23, 45, 9, 78, 56, 3, 19, 88, 14, 67, 2, 41, 5, 90, 32, 11, 54};

    cout << "Original array" << endl;
    print_array();

    cout << "Sorted array" << endl;
    print_array();

    return 0;
}

void merge_sort(int& arr, int l, int mid, int r) {
    if (l >= r) return;

    int mid = (l + r) / 2;

    merge_sort(arr, l, mid);
    merge_sort(arr, mid + 1, r);
    // Call merge
}