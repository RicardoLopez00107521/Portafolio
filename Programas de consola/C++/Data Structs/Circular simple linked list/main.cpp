#include <iostream>

using namespace std;

#include "functions.hpp"

Circular_simple_linked_list circular_simple_linked_list ;

int main() {

    circular_simple_linked_list.insert_node_in_list(3);
    circular_simple_linked_list.insert_node_in_list(2);
    circular_simple_linked_list.insert_node_in_list(1);

    circular_simple_linked_list.print_list();
    cout << "\n";

    return 0;
}