#include <iostream>
#include <vector>

using namespace std; 

void merge_sort(vector<int>&, int l, int r);
void merge(vector<int>&, int l, int mid, int r);
void print_array(vector<int>& arr); 

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
    print_array(arr);

    merge_sort(arr, 0, arr.size()-1);

    cout << "Sorted array" << endl;
    print_array(arr);

    return 0;
}

void merge_sort(vector<int>& arr, int l, int r) {
    if (l >= r) return;

    int mid = (l + r) / 2;

    merge_sort(arr, l, mid);
    merge_sort(arr, mid + 1, r);
    merge(arr, l, mid, r);
}

void merge(vector<int>& arr, int l, int mid, int r) {
    int n1 = mid - l + 1;
    int n2 = r - mid;

    vector<int> left(n1), right(n2);

    for (int i = 0; i < n1; ++i) 
        left[i] = arr[l + i];

    for (int j = 0; j < n2; j++){
        right[j] = arr[(mid+1) + j];
    }

    int i = 0, j = 0, k = l;

    while (i < n1 && j < n2) {
        if (left[i] <= right[j]) {
            arr[k] = left[i];
            ++i;
        } else {
            arr[k] = right[j];
            ++j;
        }
        ++k;
    }

    while (i < n1) {
        arr[k] = left[i];
        ++i; 
        ++k;
    }
    
    while (j < n2) {
        arr[k] = right[j];
        ++j; 
        ++k;
    }
}

void print_array(vector<int>& arr) {
    for (int i = 0; i < arr.size(); i++) 
        cout << arr[i] << " ";
    
    cout << endl;
}