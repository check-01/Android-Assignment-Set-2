import java.util.*;

public class DependencyChecker {

    public boolean hasCircularDependency(int n, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

        for (int[] edge : edges)
            graph.get(edge[0]).add(edge[1]);

        int[] visited = new int[n]; // 0 = unvisited, 1 = visiting, 2 = visited

        for (int i = 0; i < n; i++) {
            if (dfs(i, graph, visited)) return true;
        }
        return false;
    }

    private boolean dfs(int node, List<List<Integer>> graph, int[] visited) {
        if (visited[node] == 1) return true; // found a cycle
        if (visited[node] == 2) return false;

        visited[node] = 1;
        for (int neighbor : graph.get(node)) {
            if (dfs(neighbor, graph, visited)) return true;
        }
        visited[node] = 2;
        return false;
    }
}