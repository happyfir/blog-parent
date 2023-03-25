public class test {
    public node reverse(node head){
        node cur = head;
        node pre = null;
        node next = null;
        while(cur != null){
            next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    public void test(){
        node n1 = new node(1,null);
        node n2 = new node(2,null);
        node n3 = new node(3,null);
        n1.next = n2;
        n2.next = n3;
        node pre = reverse(n1);
        while(pre != null){
            System.out.println(pre.val);
            pre = pre.next;
        }
    }

}

class node {
    int val;
    node next;

    public node() {
    }

    public node(int _val, node _next) {
        val = _val;
        next = _next;
    }
}

