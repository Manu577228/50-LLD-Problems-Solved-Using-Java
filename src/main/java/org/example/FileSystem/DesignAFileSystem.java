//Design an in-memory File System similar to Linux/Windows that supports directories and files.
//The system should allow creating, deleting, listing, and reading files/directories
//while maintaining a hierarchical structure.

import java.util.*;

abstract class Node {
    String name;
    DirectoryNode parent;

    Node(String name) {
        this.name = name;
    }

    abstract boolean isDirectory();
}

class FileNode extends Node {
    String content;

    FileNode(String name) {
        super(name);
        this.content = "";
    }

    boolean isDirectory() {
        return false;
    }
}

class DirectoryNode extends Node {
    Map<String, Node> children = new HashMap<>();

    DirectoryNode(String name) {
        super(name);
    }

    boolean isDirectory() {
        return true;
    }
}

class FileSystem {
    private DirectoryNode root;

    FileSystem() {
        root = new DirectoryNode("/");
    }

    public void mkdir(String path) {
        traverseAndCreate(path, true);
    }

    public void createFile(String path, String content) {
        Node node = traverseAndCreate(path, false);
        ((FileNode) node).content = content;
    }

    public String readFile(String path) {
        Node node = traverse(path);
        return ((FileNode) node).content;
    }

    public List<String> ls(String path) {
        Node node = traverse(path);
        List<String> result = new ArrayList<>();
        if (node.isDirectory()) {
            result.addAll(((DirectoryNode) node).children.keySet());
        } else {
            result.add(node.name);
        }

        return result;
    }

    private Node traverse(String path) {
        String[] parts = path.split("/");
        Node curr = root;

        for (int i = 1; i < parts.length; i++) {
            curr = ((DirectoryNode) curr).children.get(parts[i]);
        }

        return curr;
    }

    private Node traverseAndCreate(String path, boolean isDir) {
        String[] parts = path.split("/");
        DirectoryNode curr = root;

        for (int i = 1; i < parts.length - 1; i++) {
            if (!curr.children.containsKey(parts[i])) {
                DirectoryNode dir = new DirectoryNode(parts[i]);
                dir.parent = curr;
                curr.children.put(parts[i], dir);
            }
            curr = (DirectoryNode) curr.children.get(parts[i]);
        }

        String name = parts[parts.length - 1];

        if (isDir) {
            curr.children.putIfAbsent(name, new DirectoryNode(name));
            return curr.children.get(name);
        } else {
            FileNode file = new FileNode(name);
            file.parent = curr;
            curr.children.put(name, file);
            return file;
        }
    }

    public static void main(String[] args) {

        FileSystem fs = new FileSystem();

        // Random input operations
        fs.mkdir("/a/b");
        fs.createFile("/a/b/file.txt", "Hello Bharadwaj");
        fs.createFile("/a/b/code.java", "System.out.println()");

        // Output
        System.out.println(fs.ls("/a/b"));
        System.out.println(fs.readFile("/a/b/file.txt"));
    }
}
