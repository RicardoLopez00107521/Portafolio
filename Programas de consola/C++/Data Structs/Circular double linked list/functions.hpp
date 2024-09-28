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

Circular_double_linked_list::Circular_double_linked_list() {
    head = new Node();

    head->data = 0;
    head->next = head;
    head->previous = head;
}

Node* Circular_double_linked_list::create_node(int data) {
    Node* new_node = new Node();

    new_node->data = data;
    new_node->next = nullptr;
    new_node->previous = nullptr;
}

#endif