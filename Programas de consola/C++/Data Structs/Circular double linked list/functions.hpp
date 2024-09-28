#ifndef FUNCTIONS_HPP
#define FUNCTIONS_HPP

struct Node {
    int data;
    Node* next;
    Node* previous;
};

class Circular_double_linked_list 
{
    private:
        Node* head;

    public:
        Circular_double_linked_list();
        Node* create_node(int);
        void insert_node_in_list(int);
        Node* search_node(int);
        void update_node_in_list(int, int);
        void delete_node_in_list(int);
        void print_list_left_right();
        Node* obtain_last_node();
        void print_list_right_left();
        //~Circular_double_linked_list();
};

#endif