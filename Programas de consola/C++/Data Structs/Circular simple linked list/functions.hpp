#ifndef FUNCTIONS_HPP
#define FUNCTIONS_HPP

struct Node
{
    int data;
    Node* next;
};

class Circular_simple_linked_list {
    private:
        Node *head;
    
    public:
        Circular_simple_linked_list();
        Node* create_node(int);
        void insert_node_in_list(int);
        Node* search_node(int);
        Node* search_previous_node(int);
        void update_node_in_list(int, int);
        void delete_node_in_list(int);
        void print_list();
        //~Circular_simple_linked_list();
};

Circular_simple_linked_list::Circular_simple_linked_list() {
    head = new Node();

    head->data = 0;
    head->next = nullptr;
}

Node* Circular_simple_linked_list::create_node(int data) {
    Node* new_node = new Node();

    new_node->data = data;
    new_node->next = nullptr;

    return new_node;
}

#endif