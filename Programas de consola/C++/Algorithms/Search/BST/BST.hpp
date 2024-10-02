#ifndef BST_HPP
#define BST_HPP

struct Node {
    int data;
    Node* left;
    Node* right;
};

class BST {
    private:
        Node* root;
        Node* create_node(int);
        Node* insert_implementation(Node*, int);
        Node* search_implementation(Node*, int);
        Node* get_predecessor_implementation(Node*, int);
        void print_in_order(Node*);

    public:
        BST();
        void insert_element(int);
        void search_element(int);
        void get_predecessor(int);
        void delete_element(int);
        void print();
        
};

BST::BST() {
    root = NULL;
}

Node* BST::create_node(int data) {
    Node* new_node = new Node();

    new_node->data = data;
    new_node->left = nullptr;
    new_node->right = nullptr;
}

void BST::insert_element(int data) {
    if (root == NULL) { // If the root doesn't exist, create!
        root = create_node(data);
    } else 
        insert_implementation(root, data);
}

Node* BST::insert_implementation(Node* current_node, int data) {

    if (current_node == NULL) {
        return create_node(data);
    }

    if (current_node->data == data) {
        return current_node;
    }

    if (data < current_node->data) 
        current_node->left = insert_implementation(current_node->left, data);

    else 
        current_node->right = insert_implementation(current_node->right, data);  
}

void BST::print() {
    print_in_order(root);
}

void BST::print_in_order(Node* print_node) {
    
    if (root == NULL) {
        cout << "The tree is empty!" << endl;
    } else {
        if (print_node != NULL) {
            print_in_order(print_node->left);
            cout << print_node->data << " ";
            print_in_order(print_node->right);
        }
    }  
}

void BST::search_element(int data) {
    if (root == NULL) 
        cout << "The tree is empty!" << endl;

    else {
        Node* searched_node = search_implementation(root, data);

        if (searched_node == NULL) 
            cout << "The element doesn't exist!" << endl;

        else
            cout << "Element founded: " << searched_node->data;
    }
}

Node* BST::search_implementation(Node* current_node, int data) {
    if (current_node == NULL || current_node->data == data) return current_node;
    
    if (data < current_node->data) 
        return search_implementation(current_node->left, data);

    else
        return search_implementation(current_node->right, data);
}

Node* BST::get_predecessor_implementation(Node* current_node, int data) {
    
    if (current_node->left->data == data || current_node->right->data == data) {
        return current_node;
    }
    
    if (data < current_node->data) { 
        get_predecessor_implementation(current_node->left, data);
    }

    else {
        get_predecessor_implementation(current_node->right, data);
    }
}

void BST::get_predecessor(int data) {
    Node* sucessor = get_predecessor_implementation(root, data);
}

#endif