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
        Node* max_value_of_left_tree(Node*);
        void print_in_order(Node*);

    public:
        BST();
        void algo();
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
    
    if (current_node->left != nullptr  && current_node->right != nullptr)
    {
        if (current_node->left->data == data || current_node->right->data == data) return current_node;

    } else if (current_node->left != nullptr) {
        if (current_node->left->data == data) return current_node;;
    } else
        if (current_node->right->data == data) return current_node;
    
    if (data < current_node->data) { 
        get_predecessor_implementation(current_node->left, data);
    }

    else {
        get_predecessor_implementation(current_node->right, data);
    }
}

void BST::get_predecessor(int data) {
    Node* predecessor = get_predecessor_implementation(root, data);
}

Node* BST::max_value_of_left_tree(Node* root) {
    while (root->right != nullptr) {
        root = root->right;
    }
    return root;
}

void BST::delete_element(int data) {
    Node* node_to_delete = search_implementation(root, data);
    Node* previous_node = get_predecessor_implementation(root, data);

    if (node_to_delete == NULL) { 
        cout << "The element doesn't exist!" << endl;
    } 

    else {

        if (node_to_delete->left == nullptr && node_to_delete->right == nullptr) { // Case 1
            
            if (previous_node->left == node_to_delete) 
                previous_node->left = nullptr;
            else
                previous_node->right = nullptr;

            delete(node_to_delete);           
        }
        
        
        else if (node_to_delete->left == nullptr) { // Case 2.1
            if (node_to_delete == previous_node->left) {
                previous_node->left = node_to_delete->right;
                node_to_delete->right = nullptr;
            }
            else
                previous_node->right = node_to_delete->right;
            
            delete(node_to_delete);
        }

        else if (node_to_delete->right == nullptr) { // Case 2.2
            if (node_to_delete == previous_node->left) {
                previous_node->left = node_to_delete->left;
                node_to_delete->left = nullptr;
            }
            else
                previous_node->left = node_to_delete->right;
                node_to_delete->right = nullptr;
            
            delete(node_to_delete);
        }

        else { // Case 3 tiene dos hijos
            Node* max_of_left_tree = max_value_of_left_tree(node_to_delete);
            Node* previous_of_max = get_predecessor_implementation(max_of_left_tree, max_of_left_tree->data);
        }
    }  
}

void BST::algo() {
    max_value_of_left_tree(root->left);
}

#endif