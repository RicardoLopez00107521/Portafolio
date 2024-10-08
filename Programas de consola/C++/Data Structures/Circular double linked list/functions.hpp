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
        void print_list_right_left();
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

void Circular_double_linked_list::insert_node_in_list(int data) {
    Node* node_to_insert = create_node(data);
    Node* current_node = head;

    while (current_node->next != head && current_node->next->data < node_to_insert->data) {
        current_node = current_node->next;
    }

    if (current_node->next == head) {
        node_to_insert->next = current_node->next;
        node_to_insert->previous = current_node;

        current_node->next = node_to_insert;
        head->previous = node_to_insert;
    } else {
        node_to_insert->next = current_node->next;
        node_to_insert->previous = current_node;

        current_node->next->previous = node_to_insert;
        current_node->next = node_to_insert;
    }    
}

void Circular_double_linked_list::print_list_left_right() {
    Node* print_node = head->next;

    if (head->next == head) {
        cout << "The list is empty!" << endl;
    } else {
        while (print_node != head) {
            cout << print_node->data << " ";
            print_node = print_node->next;
        }
        cout << print_node->next->data << endl;
    }
}

void Circular_double_linked_list::print_list_right_left() {
    Node* print_node = head->previous;

    if (head->previous == head) {
        cout << "The list is empty!" << endl;
    } else {
        while (print_node != head) {
            cout << print_node->data << " ";
            print_node = print_node->previous;
        }
        cout << print_node->previous->data << endl;
    }
}

Node* Circular_double_linked_list::search_node(int data) {
    Node* searched_node = head->next;

    while (searched_node != head && searched_node->data != data) {
        searched_node = searched_node->next;
    }

    return searched_node;
}

void Circular_double_linked_list::update_node_in_list(int data, int new_data) {
    Node* node_to_update = search_node(data);

    if (node_to_update != head) {
        delete_node_in_list(data);
        insert_node_in_list(new_data);
    } else
        cout << "The element not exist!" << endl;
}

void Circular_double_linked_list::delete_node_in_list(int data) {
    Node* node_to_delete = search_node(data);
    Node* previous_node = node_to_delete->previous;

    if (node_to_delete != head) {
        
        if (previous_node->next == head) {
            previous_node->next->next = node_to_delete->next; 
            node_to_delete->next->previous = previous_node;

            node_to_delete->next = nullptr;
            node_to_delete->previous = nullptr;

            delete(node_to_delete);

        } else {
            previous_node->next = node_to_delete->next;
            previous_node->next->previous = previous_node;
            
            node_to_delete->next = nullptr;
            node_to_delete->previous = nullptr;

            delete(node_to_delete);
        }
    } else
        cout << "The element not exist!\n";
}

#endif