#include <iostream>

using namespace std;

#include "functions.hpp"

Circular_simple_linked_list circular_simple_linked_list ;

int main() {

    circular_simple_linked_list.insert_node_in_list(3);
    circular_simple_linked_list.insert_node_in_list(2);
    circular_simple_linked_list.insert_node_in_list(1);
    circular_simple_linked_list.insert_node_in_list(4);
    circular_simple_linked_list.insert_node_in_list(5);
    circular_simple_linked_list.insert_node_in_list(6);

    circular_simple_linked_list.print_list();
    cout << "\n";

    circular_simple_linked_list.search_previous_node(7);

    return 0;
}