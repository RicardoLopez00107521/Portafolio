#ifndef FUNCTIONS_HPP
#define FUNCTIONS_HPP

struct Node
{
    int dato;
    Node* next;
};

class Circular_simple_linked_list {
    private:
        Node *head;
    
    public:
        Circular_simple_linked_list();
        Node* create_node(int data);
        void insert_node_in_list(int data);
        Node* search_node(int data);
        Node* search_previous_node(int data);
        void update_node_in_list(int data, int new_data);
        void delete_node_in_list(int data);
        void print_list();
        //~Circular_simple_linked_list();
};

#endif