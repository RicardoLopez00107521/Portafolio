#ifndef FUNCTIONS_HPP
#define FUNCTIONS_HPP

struct Node {
    int data;
    Node* next;
};

class Simple_linked_list {
    private:
        Node* head;
        int _size = 0;

    public:
        Simple_linked_list();
        Node* create_node(int data);
        void insert_node_in_list(Node*);
        void update_node_in_list(int data, int new_data);
        void delete_node_in_list(int data);
        void print_list();
        //~Simple_linked_list();
};

#endif