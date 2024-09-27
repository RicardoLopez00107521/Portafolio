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
        void print_list_left_right();
        Node* obtain_last_node();
        void print_list_right_left();
        //~Double_linked_list();
};

Double_linked_list::Double_linked_list() {
    head = new Node();

    head->data = 0;
    head->next = nullptr;
    head->previous = nullptr;
}

Node* Double_linked_list::create_node(int data) {
    Node* new_node = new Node();

    new_node->data = data;
    new_node->previous = nullptr;
    new_node->next = nullptr;
}

void Double_linked_list::insert_node_in_list(int data) {
    Node* node_to_insert = create_node(data);
    Node* current_node = head;

    while (current_node->next != nullptr && current_node->next->data < node_to_insert->data) {
        current_node = current_node->next;
    }
    
    if(current_node == head || current_node->next == nullptr) {
        node_to_insert->previous = current_node;
        current_node->next = node_to_insert;
    } else {
        node_to_insert->next = current_node->next;
        current_node->next->previous = node_to_insert;

        current_node->next = node_to_insert;
        node_to_insert->previous = current_node;
    }
}

void Double_linked_list::print_list_left_right() {
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

Node* Double_linked_list::obtain_last_node() {
    Node* last_node = head->next;

    while (last_node->next != NULL) {
        last_node = last_node->next;
    }

    return last_node;
}

void Double_linked_list::print_list_right_left() {
    Node* print_node = obtain_last_node();

    if (head->next == NULL)
    {
        cout << "The list is empty!" << endl;
    } else {
        while(print_node != head) {
            cout << print_node->data << " ";
            print_node = print_node->previous;
        }
    }
}

#endif