#include <iostream>

using namespace std;

#include "BST.hpp"

BST bst;

int main() {

    bst.insert_element(100);
    bst.insert_element(20);
    bst.insert_element(10);
    bst.insert_element(30);
    bst.insert_element(500);
    bst.print();

    cout << endl;

    bst.insert_element(2);
    bst.print();

    cout << endl;

    bst.search_element(0);
    cout << endl;
    

    return 0;
}