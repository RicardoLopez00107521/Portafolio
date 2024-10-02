#ifndef BST_HPP
#define BST_HPP

struct Node {
    int data;
    Node* left;
    Node* riight;
};

class BST {
    private:
        Node* root;
        Node* create_node(int);
        Node* insert_implementation(Node*, int);
        void print_in_order(Node*);

    public:
        BST();
        void insert_element(int);
        Node* search_element(int);
        Node* get_succesor(int);
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
    new_node->riight = nullptr;
}

void BST::insert_element(int data) {
    insert_implementation(root, data);
}

Node* BST::insert_implementation(Node* current_node, int data) {
    if (current_node == NULL) {
        return root = create_node(data);
    }

    if (current_node->data == data) {
        return current_node;
    }
}

void BST::print(){
    print_in_order(root);
}

void BST::print_in_order(Node* print_node) {
    if (root == NULL) {
        cout << "The tree is empty!" << endl;
    } else {
        if (print_node != NULL) {
            print_in_order(print_node->left);
            cout << print_node->data << " ";
            print_in_order(print_node->riight);
        }
    }     
}


#endif