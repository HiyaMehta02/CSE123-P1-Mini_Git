// Hiya Mehta
// 02/11/2025
// CSE 123
// TA : Hayden Feeney 

import java.util.*;
import java.text.SimpleDateFormat;

// This is a Repository class that stores different versions of your work. 
// It allows you to access the most recent commit, get the size of the repo,
// view the first repo as a string, access, the most front commit, check if 
// the repo contains a certain commit, access the commit history, commit new 
// work, drop commits, and merge two repositories together.
public class Repository {
    private Commit front;
    private String repoName;
    
    // this is a constructor
    // Behavior :
    //     - Creates a new Repository and initializes the name
    // Parameters : 
    //     - name (String) : the name for the Repository 
    // Returns : None
    // Exceptions : 
    //     - throws an IllegalArgumentException if the name is null or an
    //       empty string
    public Repository(String name) {
        if (name == null || name == "") {
            throw new IllegalArgumentException();
        }
        this.repoName = name;
    }

    // Behavior : 
    //    - gets the id of the most recent commit
    // Parameters : None
    // Returns : 
    //    - String : returns the id of the most recent commit
    //               if head is null it returns null
    // Exceptions : None 
    public String getRepoHead() {
        if (front == null) {
            return null;
        } else {
            return front.id;
        }
    }

    // Behavior : 
    //    - gets size of the repo (the amount of commits)
    // Parameters : None
    // Returns : 
    //    - int : returns the size of the repo (the amount of commits)
    // Exceptions : None 
    public int getRepoSize() {
        Commit temp = front;
        int size = 0;
        while (temp != null) {
            size++;
            temp = temp.past;
        }
        return size;
    }

    // Behavior : 
    //    - converts the most recent commit to a string
    //    - if the repository is empty it returns the name of the repo
    //      and says there are no commits
    // Parameters : None
    // Returns : 
    //    - String : returns the most recent commit as a string
    // Exceptions : None
    public String toString() {
        if (front == null) {
            return repoName + " - No commits";
        }
        return repoName + " - Current head: " + front.toString();
    }

    // Behavior : 
    //    - gets the most recent Commit
    // Parameters : None
    // Returns : 
    //    - Commit : returns the most recent Commit
    // Exceptions : None
    private Commit getFrontCommit() {
        return front;
    }

    // Behavior : 
    //    - checks if the repo contains a commit based on commit id
    // Parameters : 
    //    - String : the id for the commit they are looking for
    // Returns : 
    //    - boolean : 
    //         - true if the repo does contain the commit
    //         - false if the repo does not contain the commit
    // Exceptions : 
    //    - if targetId is null then it throws an 
    //      IllegalArgumentException()
    public boolean contains(String targetId) {
        if (targetId == null) {
            throw new IllegalArgumentException();
        }
        if (front != null) {
            Commit temp = front;
            while (temp.past != null || temp.id.equals(targetId)) {
                if (temp.id.equals(targetId)) {
                    return true;
                }
                temp = temp.past;
            }
        } 
        return false;
    }

    // Behavior : 
    //    - gets the history of the repo 
    //    - if n is greater that then amount of commits, it will
    //      return all the commits availible
    // Parameters : 
    //    - int : the amount number most recent commits the user wants to see
    // Returns : 
    //    - String : n of the most recent commits
    // Exceptions : 
    //    - throws IllegalArgumentException if n is less than or 
    //      equal to 0
    public String getHistory(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        String nCommits = "";
        Commit temp = front;
        if (temp != null) {
            int count = 0;
            while (temp != null && count < n) {
                nCommits += temp.toString() + "\n";
                temp = temp.past;
                count++;
            } 
        } 
        return nCommits;
    }

    // Behavior : 
    //    - adds a new commit 
    // Parameters : 
    //    - String : the message that the user wants to associate with the commit
    // Returns : 
    //    - String : the id of the commit just made
    // Exceptions : 
    //    - throws IllegalArgumentException the message is null
    public String commit(String message) {
        if (message == null) {
            throw new IllegalArgumentException();
        }
        Commit commit = new Commit(message, front);
        front = commit;
        return front.id;
    }


    // Behavior : 
    //    - drops the commit the user wants droped 
    // Parameters : 
    //    - String : the id of the commit that the user wants to drop
    // Returns : 
    //    - boolean : 
    //         - true if the commit was dropped
    //         - false if the commit was not dropped or not found
    // Exceptions : 
    //    - throws IllegalArgumentException the targetId is null
    public boolean drop(String targetId) {
        if (targetId == null) {
            throw new IllegalArgumentException();
        }
        if (front == null) {
            return false;
        }
        if (front.id.equals(targetId)) {
            front = front.past;
            return true;
        }
        Commit temp = front;
        while (temp.past != null) {
            if (temp.past.id.equals(targetId)) {
                temp.past = temp.past.past;
                return true;
            }
            temp = temp.past;
        }
        return false;
    }

    // Behavior : 
    //    - merges two repos together based on the time that the 
    //      commits were made 
    //    - it also leaves the other repository empty after 
    //      being merged
    // Parameters : 
    //    - Repository : the repo they want to merge with this one
    // Returns : None
    // Exceptions : 
    //    - throws IllegalArgumentException the other repo is null
    public void synchronize(Repository other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        if (this.front == null) {
            this.front = other.getFrontCommit();
        } else if (other.getFrontCommit() != null) {
            Commit temp = new Commit("temp");
            Commit current = temp;
            Commit thisCommit = this.front;
            Commit otherCommit = other.getFrontCommit();
            while (thisCommit != null && otherCommit != null) {
                if (thisCommit.timeStamp >= otherCommit.timeStamp) {
                    current.past = thisCommit;
                    thisCommit = thisCommit.past;
                } else {
                    current.past = otherCommit;
                    otherCommit = otherCommit.past;
                }
                current = current.past;
            }
            if (thisCommit != null) {
                current.past = thisCommit;
            } else {
                current.past = otherCommit;
            }
            this.front = temp.past;
        }
        while (other.getFrontCommit() != null) {
            other.drop(other.getRepoHead());
        }
    }

    /**
     * DO NOT MODIFY
     * A class that represents a single commit in the repository.
     * Commits are characterized by an identifier, a commit message,
     * and the time that the commit was made. A commit also stores
     * a reference to the immediately previous commit if it exists.
     *
     * Staff Note: You may notice that the comments in this 
     * class openly mention the fields of the class. This is fine 
     * because the fields of the Commit class are public. In general, 
     * be careful about revealing implementation details!
     */
    public static class Commit {

        private static int currentCommitID;

        /**
         * The time, in milliseconds, at which this commit was created.
         */
        public final long timeStamp;

        /**
         * A unique identifier for this commit.
         */
        public final String id;

        /**
         * A message describing the changes made in this commit.
         */
        public final String message;

        /**
         * A reference to the previous commit, if it exists. Otherwise, null.
         */
        public Commit past;

        /**
         * Constructs a commit object. The unique identifier and timestamp
         * are automatically generated.
         * @param message A message describing the changes made in this commit. Should be non-null.
         * @param past A reference to the commit made immediately before this
         *             commit.
         */
        public Commit(String message, Commit past) {
            this.id = "" + currentCommitID++;
            this.message = message;
            this.timeStamp = System.currentTimeMillis();
            this.past = past;
        }

        /**
         * Constructs a commit object with no previous commit. The unique
         * identifier and timestamp are automatically generated.
         * @param message A message describing the changes made in this commit. Should be non-null.
         */
        public Commit(String message) {
            this(message, null);
        }

        /**
         * Returns a string representation of this commit. The string
         * representation consists of this commit's unique identifier,
         * timestamp, and message, in the following form:
         *      "[identifier] at [timestamp]: [message]"
         * @return The string representation of this collection.
         */
        @Override
        public String toString() {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(timeStamp);

            return id + " at " + formatter.format(date) + ": " + message;
        }

        /**
        * Resets the IDs of the commit nodes such that they reset to 0.
        * Primarily for testing purposes.
        */
        public static void resetIds() {
            Commit.currentCommitID = 0;
        }
    }
}
