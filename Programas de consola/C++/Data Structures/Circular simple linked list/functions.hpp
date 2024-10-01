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

    if (head->next == head)
    {
        cout << "The list is empty!" << endl;
    } else {
        while(print_node != head) {
            cout << print_node->data << " ";
            print_node = print_node->next;
        }
        cout << print_node->next->data;
    }
}

Node* Circular_simple_linked_list::search_node(int data) {
    Node* searched_node = head->next;

    while (searched_node != head && searched_node->data != data) {
        searched_node = searched_node->next;
    }    

    return searched_node;   
}

Node* Circular_simple_linked_list::search_previous_node(int data) {
    Node* searched_node = head->next;
    Node* aux_node = nullptr;

    if (searched_node->data != data) {

        while (searched_node != head && searched_node->data != data) {
            aux_node = searched_node;
            searched_node = searched_node->next;
        } 

        if (searched_node == head) {
            return nullptr;
        } else return aux_node;
          
    } else {
            while (searched_node->next->data != data) {
                aux_node = searched_node;
                searched_node = searched_node->next;
            } 
            return aux_node;
        }   
}

void Circular_simple_linked_list::update_node_in_list(int data, int new_data) {
    Node* node_to_update = search_node(data);

    if (node_to_update != head) {
        delete_node_in_list(data);
        insert_node_in_list(new_data);
    } else
        cout << "The element not exist!" << endl;
}

void Circular_simple_linked_list::delete_node_in_list(int data) {
    Node* node_to_delete = search_node(data);

    if (node_to_delete != head) {
        Node* previous_node = search_previous_node(data);

        if (previous_node->next == head) {
            previous_node->next->next = node_to_delete->next;
            node_to_delete->next = nullptr;
            delete(node_to_delete);
            
        } else {
            previous_node->next = node_to_delete->next;
            node_to_delete->next = nullptr;
            delete(node_to_delete);
        }

    } else 
        cout << "The element not exist!\n";
}

#endif