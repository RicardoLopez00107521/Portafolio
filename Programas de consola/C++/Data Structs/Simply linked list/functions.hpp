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
        void insert_node_in_list(int data);
        Node* search_node(int data);
        void update_node_in_list(int data, int new_data);
        void delete_node_in_list(int data);
        void print_list();
        //~Simple_linked_list();
};

Simple_linked_list::Simple_linked_list() {
    head = new Node();

    head->data = 0;
    head->next = nullptr;
}

Node* Simple_linked_list::create_node(int data) {
    Node* new_node = new Node;

    new_node->data = data;
    new_node->next = nullptr;

    return new_node;
}

void Simple_linked_list::insert_node_in_list(int data) {
    Node* new_node = create_node(data);
    Node* current_node = head;

    while (current_node->next != nullptr && current_node->next->data < new_node->data) {
        current_node = current_node->next;  
    }

    new_node->next = current_node->next;
    current_node->next = new_node;
}

Node* Simple_linked_list::search_node(int data) {
    Node* searched_node = head->next;
    Node* return_node = searched_node->next;

    while (searched_node != nullptr && searched_node->data != data) {
        searched_node = searched_node->next;
    }

    return searched_node;    
}

void Simple_linked_list::update_node_in_list(int data, int new_data) {
    Node* node_to_update = search_node(data);

    if (node_to_update != nullptr) {
        node_to_update->data = new_data;
    } else
        cout << "The element not exist!\n";
}

void Simple_linked_list::print_list() {
    Node* print_node = head->next;

    if (head->next == NULL)
    {
        cout << "The list is empty!" << endl; 
    } else {
        while(print_node != NULL) {
            cout << print_node->data << " ";
            print_node = print_node->next;
        }
    }  
}

#endif