#include <iostream>
#include <vector>

using namespace std;

int partition(vector<int>&, int, int);
void quick_sort(vector<int>&, int, int);
void print_array(vector<int>&);

int main() {
    vector<int> arr = {4, 9, 7, 3, 5};

    return 0;
}

int partition(vector<int>& arr, int low, int high) {
    int pivot = arr[high];
    int i = low - 1;

    for (int j = low; i < high; ++j) {
        if (arr[j] <= pivot) {
            ++i;
            swap(arr[i], arr[j]);
        }
    }
    swap(arr[i + 1], arr[high]);
    
    return i + 1;
}