#include <iostream>

using namespace std;

#include "functions.hpp"

Circular_double_linked_list circular_double_linked_list;

int main () {

    circular_double_linked_list.insert_node_in_list(2);
    circular_double_linked_list.insert_node_in_list(1);
    circular_double_linked_list.insert_node_in_list(3);
    circular_double_linked_list.insert_node_in_list(4);
    circular_double_linked_list.insert_node_in_list(5);

    circular_double_linked_list.print_list_left_right();
    circular_double_linked_list.print_list_right_left();

    circular_double_linked_list.search_node(0);

    circular_double_linked_list.delete_node_in_list(0);

    circular_double_linked_list.print_list_left_right();
    circular_double_linked_list.print_list_right_left();

    circular_double_linked_list.update_node_in_list(2, 0);

    circular_double_linked_list.print_list_left_right();
    circular_double_linked_list.print_list_right_left();

    return 0; 
}