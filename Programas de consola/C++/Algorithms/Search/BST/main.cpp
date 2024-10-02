#include <iostream>

using namespace std;

#include "BST.hpp"

BST bst;

int main() {

    bst.insert_element(2);
    bst.insert_element(1);
    bst.insert_element(3);
    bst.print();

    cout << endl;

    bst.insert_element(2);
    bst.print();

    return 0;
}