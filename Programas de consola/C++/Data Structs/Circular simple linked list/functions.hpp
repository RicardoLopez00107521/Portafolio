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
    head->next = head;
}

Node* Circular_simple_linked_list::create_node(int data) {
    Node* new_node = new Node();

    new_node->data = data;
    new_node->next = nullptr;

    return new_node;
}

void Circular_simple_linked_list::insert_node_in_list(int data) {
    Node* node_to_insert = create_node(data);
    Node* current_node = head;

    while (current_node->next != head && current_node->next->data < node_to_insert->data) {
        current_node = current_node->next;
    }
    
    node_to_insert->next = current_node->next;
    current_node->next = node_to_insert;
}

void Circular_simple_linked_list::print_list() {
    Node* print_node = head->next;

    if (head->next == NULL)
    {
        cout << "The list is empty!" << endl;
    } else {
        while(print_node != head) {
            cout << print_node->data << " ";
            print_node = print_node->next;
        }
    }
    cout << print_node->next->data;
}

Node* Circular_simple_linked_list::search_node(int data) {
    Node* searched_node = head->next;

    while (searched_node != head && searched_node->data != data) {
        searched_node = searched_node->next;
    }    

    return searched_node;   
}

#endif