#include <iostream>

using namespace std;

#include "BST.hpp"

BST bst;

int main() {

    bst.insert_element(5);

    bst.print_in_order();
    
    return 0;
}