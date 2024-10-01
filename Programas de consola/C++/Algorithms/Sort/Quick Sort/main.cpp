#include <iostream>
#include <vector>

using namespace std;

int partition(vector<int>&, int, int);
void quick_sort(vector<int>&, int, int);
void print_array(vector<int>&);

int main() {
    vector<int> arr = {4, 9, 7, 3, 5};

    cout << "Original array" << endl;
    print_array(arr);

    quick_sort(arr, 0, arr.size() - 1);

    cout << "Sorted array" << endl;
    print_array(arr);

    return 0;
}

int partition(vector<int>& arr, int low, int high) {
    int pivot = arr[high];
    int i = low - 1;

    for (int j = low; j < high; ++j) {
        if (arr[j] <= pivot) {
            ++i; 
            swap(arr[i], arr[j]); 
        }
    }
    swap(arr[i + 1], arr[high]);

    return i + 1;
}

void quick_sort(vector<int>& arr, int low, int high) {
    if (low > high) return;

    int pivot = partition(arr, low, high);

    quick_sort(arr, low, pivot - 1);
    quick_sort(arr, pivot + 1, high);
}

void print_array(vector<int>& arr) {
    for (int i = 0; i < arr.size(); ++i)
        cout << arr[i] << " ";

    cout << endl;
}