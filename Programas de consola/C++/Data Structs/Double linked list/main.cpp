#include <iostream>

using namespace std;

#include "functions.hpp"

Double_linked_list double_linked_list;

int main () {
    
    double_linked_list.insert_node_in_list(1);
    double_linked_list.insert_node_in_list(3);
    double_linked_list.insert_node_in_list(5);
    double_linked_list.insert_node_in_list(2);
    double_linked_list.insert_node_in_list(4);

    double_linked_list.print_list_left_right();
    cout << "\n";
    
    double_linked_list.print_list_right_left();
    cout << "\n";

    double_linked_list.delete_node_in_list(1);
    
    double_linked_list.print_list_left_right(); 
    cout << "\n";

    double_linked_list.print_list_right_left();
    cout << "\n";

    double_linked_list.update_node_in_list(2,9);
    
    double_linked_list.print_list_left_right();    
    cout << "\n";

    double_linked_list.print_list_right_left();
    cout << "\n";

    double_linked_list.~Double_linked_list();

    double_linked_list.print_list_left_right();    
    cout << "\n";

    return 0;
}