#include <iostream>
#include <vector>

using namespace std; 

void merge_sort(vector<int>&, int l, int r);
void merge(vector<int>&, int l, int mid, int r);
void print_array(); 

int main() {
    vector<int> arr;
    arr.push_back(7);
    arr.push_back(8);
    arr.push_back(9);
    arr.push_back(10);
    arr.push_back(11);
    arr.push_back(3);
    arr.push_back(6);
    arr.push_back(8);

    cout << "Original array" << endl;
    print_array();

    cout << "Sorted array" << endl;
    print_array();

    return 0;
}

void merge_sort(vector<int>& arr, int l, int r) {
    if (l >= r) return;

    int mid = (l + r) / 2;

    merge_sort(arr, l, mid);
    merge_sort(arr, mid + 1, r);
    // Call merge
}