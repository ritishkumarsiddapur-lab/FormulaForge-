/**
     * Computes the horizontal character width required by a subtree with
     * guaranteed spacing so adjacent decimal/variable labels never collide.
     */
    private static int getSubtreeWidth(ExprNode node, CustomSymbolTable vars) {
        if (node == null) return 0;
        int labelLen = getNodeLabel(node, vars).length();

        if (node.left == null && node.right == null) {
            return labelLen;
        }
        if (node.left != null && node.right != null) {
            int leftW = getSubtreeWidth(node.left, vars);
            int rightW = getSubtreeWidth(node.right, vars);
            // +3 guarantees at least 3 spaces between left and right subtrees
            return Math.max(labelLen, leftW + 3 + rightW);
        }
        if (node.right != null) {
            int rightW = getSubtreeWidth(node.right, vars);
            return Math.max(labelLen, rightW + 2);
        }
        int leftW = getSubtreeWidth(node.left, vars);
        return Math.max(labelLen, leftW + 2);
    }

    /**
     * Computes height (number of node levels) of the Expression Tree.
     */
    private static int getTreeHeight(ExprNode node) {
        if (node == null) return 0;
        return 1 + Math.max(getTreeHeight(node.left), getTreeHeight(node.right));
    }

    /**
     * Renders an authentic top-down Binary Expression Tree using '/' and '\'
     * slant connectors and '(' ')' node brackets on a 2D char grid.
     */
    public static void printExpressionTree(ExprNode root, CustomSymbolTable vars) {
        System.out.println("\n--- STEP 2: BINARY EXPRESSION TREE VISUALIZATION ---");
        if (root == null) return;

        int height = getTreeHeight(root);
        int totalWidth = getSubtreeWidth(root, vars) + 2;
        int totalRows = height * 2 - 1;

        char[][] canvas = new char[totalRows][totalWidth];
        for (int r = 0; r < totalRows; r++) {
            for (int c = 0; c < totalWidth; c++) {
                canvas[r][c] = ' ';
            }
        }

        // Draw using exact non-overlapping horizontal bounds [0, totalWidth]
        drawNodeInRange(canvas, root, 0, 0, totalWidth, vars);

        for (int r = 0; r < totalRows; r++) {
            int lastNonSpace = totalWidth - 1;
            while (lastNonSpace >= 0 && canvas[r][lastNonSpace] == ' ') {
                lastNonSpace--;
            }
            if (lastNonSpace >= 0) {
                System.out.println(new String(canvas[r], 0, lastNonSpace + 1));
            }
        }
    }

    /**
     * Places a node centered inside [leftBound, rightBound) and partitions that
     * range proportionally between its left and right subtrees so nothing overlaps.
     */
    private static void drawNodeInRange(char[][] canvas, ExprNode node, int row, int leftBound, int rightBound, CustomSymbolTable vars) {
        if (node == null) return;

        String label = getNodeLabel(node, vars);
        int centerCol = (leftBound + rightBound) / 2;
        int startCol = Math.max(leftBound, centerCol - label.length() / 2);

        // Write node label characters onto the canvas row
        for (int i = 0; i < label.length() && (startCol + i) < canvas[row].length; i++) {
            canvas[row][startCol + i] = label.charAt(i);
        }

        if (node.left != null && node.right != null) {
            int leftW = getSubtreeWidth(node.left, vars);
            int rightW = getSubtreeWidth(node.right, vars);
            int totalChildW = leftW + 3 + rightW;

            int rangeWidth = rightBound - leftBound;
            int offset = Math.max(0, (rangeWidth - totalChildW) / 2);

            int lStart = leftBound + offset;
            int lEnd = lStart + leftW;
            int rStart = lEnd + 3;
            int rEnd = rStart + rightW;

            int leftCenter = (lStart + lEnd) / 2;
            int rightCenter = (rStart + rEnd) / 2;

            // Draw '/' toward left child
            int slashCol = (centerCol + leftCenter) / 2;
            if (slashCol >= centerCol) slashCol = centerCol - 1;
            if (row + 1 < canvas.length && slashCol >= 0) {
                canvas[row + 1][slashCol] = '/';
            }

            // Draw '\' toward right child
            int backslashCol = (centerCol + rightCenter + 1) / 2;
            if (backslashCol <= centerCol) backslashCol = centerCol + 1;
            if (row + 1 < canvas.length && backslashCol < canvas[row].length) {
                canvas[row + 1][backslashCol] = '\\';
            }

            drawNodeInRange(canvas, node.left, row + 2, lStart, lEnd, vars);
            drawNodeInRange(canvas, node.right, row + 2, rStart, rEnd, vars);

        } else if (node.right != null) {
            // Unary minus ('u-') has only a right child
            int rightCenter = (centerCol + rightBound) / 2;
            int backslashCol = (centerCol + rightCenter + 1) / 2;
            if (backslashCol <= centerCol) backslashCol = centerCol + 1;
            if (row + 1 < canvas.length && backslashCol < canvas[row].length) {
                canvas[row + 1][backslashCol] = '\\';
            }
            drawNodeInRange(canvas, node.right, row + 2, centerCol, rightBound, vars);

        } else if (node.left != null) {
            int leftCenter = (leftBound + centerCol) / 2;
            int slashCol = (centerCol + leftCenter) / 2;
            if (slashCol >= centerCol) slashCol = centerCol - 1;
            if (row + 1 < canvas.length && slashCol >= 0) {
                canvas[row + 1][slashCol] = '/';
            }
            drawNodeInRange(canvas, node.left, row + 2, leftBound, centerCol, vars);
        }
    }