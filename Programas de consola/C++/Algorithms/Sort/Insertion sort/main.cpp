#include <iostream>

using namespace std;

void swap(int&, int&);
void insertion_sort();
void print();

int main() {

    return 0;
}

void swap(int& a, int& b) {
    int temp = a;
    a = b;
    b = temp; 
}