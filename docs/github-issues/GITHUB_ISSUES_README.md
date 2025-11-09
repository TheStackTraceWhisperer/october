# Converting TODOs to GitHub Issues

This directory contains tools for converting the TODO documents into well-organized GitHub issues.

## Overview

Two documents have been converted into GitHub issues:
1. **integration-todos.md** - Integration tasks and priorities
2. **docs/zone-tilemap-sequence.md** - Zone, tilemap, and sequence implementation enhancements

## Script: create-issues-from-todos.sh

This script automates the creation of GitHub issues from the TODO documents.

### Prerequisites

1. **GitHub CLI (gh)** must be installed
   - Installation: https://cli.github.com/
   - Or install via package manager: `brew install gh` (macOS), `apt install gh` (Ubuntu), etc.

2. **Authentication** with GitHub
   - Option 1: Run `gh auth login` and follow the prompts
   - Option 2: Set the `GH_TOKEN` environment variable with a personal access token

### Usage

```bash
# If already authenticated with gh auth login:
./create-issues-from-todos.sh

# Or with a GitHub token:
GH_TOKEN=your_github_token ./create-issues-from-todos.sh
```

### What the Script Creates

The script creates **12 GitHub issues** organized by priority and feature area:

#### From integration-todos.md (6 issues)

1. **High Priority:**
   - Implement TilemapRenderSystem
   - Choose and Implement Tilemap Collision Strategy

2. **Medium Priority:**
   - Extend ZoneService to Parse Tilemap and Tileset Data
   - Provide Real Zone Asset with Tilemap and Triggers

3. **Low Priority:**
   - Consider Switching UI Events to Typed Event Classes
   - Add Tests for Fade Overlay Rendering and Zone JSON Loading

#### From zone-tilemap-sequence.md (6 issues)

1. **High Priority:**
   - Complete Implementation of Trigger Condition Types
   - Implement Tilemap-Based Collision Detection

2. **Medium Priority:**
   - Implement Completion Checking for Blocking Sequence Operations
   - Expand GameEvent Type Support Based on Game Needs
   - Integrate Tilemap Rendering with Main RenderSystem

3. **Low Priority:**
   - Implement Async Zone Loading with Thread Safety

### Issue Labels

Each issue is labeled with:
- `enhancement` (all issues are enhancements)
- Priority: `high-priority`, `medium-priority`, or `low-priority`
- Feature area: `rendering`, `collision`, `zone-loading`, `sequences`, `triggers`, `testing`, `ui`, `performance`, etc.

### Issue Structure

Each issue includes:
- **Description**: Clear explanation of the task
- **Requirements**: Specific technical requirements
- **Context**: Where the task came from and why it matters
- **Related Components**: Which parts of the codebase are involved
- **Acceptance Criteria**: Checklist of completion criteria

## Manual Issue Creation

If you prefer to create issues manually, refer to the script for the detailed issue descriptions and labels. Each issue is self-contained and can be created independently.

## After Creating Issues

1. Review the issues in GitHub and adjust priorities if needed
2. Organize issues into milestones for release planning
3. Assign issues to team members
4. Track progress using GitHub project boards
5. Link related issues together
6. Update issue descriptions as implementation details become clear

## Issue Organization Recommendations

### By Milestone
- **v1.0 - Core Tilemap Support**: Issues 1, 2, 3, 12
- **v1.1 - Enhanced Triggers**: Issues 7, 8
- **v1.2 - Sequence Expansion**: Issue 9
- **v2.0 - Polish & Performance**: Issues 5, 6, 10, 11

### By Feature Area
- **Rendering**: Issues 1, 11
- **Collision**: Issues 2, 12
- **Zone Loading**: Issues 3, 4, 10
- **Triggers & Sequences**: Issues 7, 8, 9
- **Testing & Quality**: Issue 6
- **UI/UX**: Issue 5

### By Dependencies
Some issues depend on others:
- Issue 11 depends on Issue 1 (tilemap rendering integration needs TilemapRenderSystem)
- Issue 12 depends on Issue 3 (collision needs tilemap parsing)
- Issue 4 depends on Issue 3 (real zone needs parsing support)

## Notes

- The original TODO documents remain in the repository for reference
- Issues are created with enough detail to be actionable
- Each issue can be worked on independently once dependencies are met
- Feel free to add comments, refinements, or sub-tasks to issues as implementation progresses
