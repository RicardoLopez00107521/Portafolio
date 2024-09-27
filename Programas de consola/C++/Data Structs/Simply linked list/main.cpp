#include <iostream>

using namespace std;

#include "functions.hpp"

Simple_linked_list simple_linked_list;

int main () {

    simple_linked_list.insert_node_in_list(0);
    simple_linked_list.insert_node_in_list(-1);
    simple_linked_list.insert_node_in_list(8);
    simple_linked_list.insert_node_in_list(3);

    simple_linked_list.print_list();
    cout << "\n";

    simple_linked_list.update_node_in_list(3, 5);
    simple_linked_list.print_list();

    return 0;
}