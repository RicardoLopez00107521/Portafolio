#ifndef FUNCTIONS_HPP
#define FUNCTIONS_HPP

struct Node {
    int data;
    Node* next;
    Node* previous;
};

class Double_linked_list {
    private:
        Node* head;
        int _size = 0;

    public:
        Double_linked_list();
        Node* create_node(int data);
        void insert_node_in_list(int data);
        Node* search_node(int data);
        void update_node_in_list(int data, int new_data);
        void delete_node_in_list(int data);
        void print_list();
        //~Double_linked_list();
};

Double_linked_list::Double_linked_list() {
    Node* head = new Node;

    head->data = 0;
    head->next = nullptr;
    head->previous = nullptr;
}

#endif