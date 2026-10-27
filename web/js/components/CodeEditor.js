/**
 * @file CodeEditor.js
 * @description Lightweight code editor wrapper managing indentation, line numbering, and theming.
 */

export class CodeEditor {
  constructor(textareaId = 'code-editor-textarea', lineColId = 'line-numbers-container') {
    this.textareaId = textareaId;
    this.lineColId = lineColId;
  }

  getTextarea() {
    return document.getElementById(this.textareaId);
  }

  getLineContainer() {
    return document.getElementById(this.lineColId);
  }

  attachListeners() {
    const textarea = this.getTextarea();
    if (!textarea) return;

    // Line number auto-sync
    textarea.addEventListener('input', () => this.updateLineNumbers());

    // Tab key interception for 4-space indentation
    textarea.addEventListener('keydown', (e) => {
      if (e.key === 'Tab') {
        e.preventDefault();
        const start = textarea.selectionStart;
        const end = textarea.selectionEnd;
        textarea.value = textarea.value.substring(0, start) + '    ' + textarea.value.substring(end);
        textarea.selectionStart = textarea.selectionEnd = start + 4;
        this.updateLineNumbers();
      }
    });

    this.updateLineNumbers();
  }

  updateLineNumbers() {
    const textarea = this.getTextarea();
    const lineCol = this.getLineContainer();
    if (!textarea || !lineCol) return;

    const lines = textarea.value.split('\n').length;
    let numbersHtml = '';
    for (let i = 1; i <= Math.max(lines, 14); i++) {
      numbersHtml += `<div>${i}</div>`;
    }
    lineCol.innerHTML = numbersHtml;
  }

  setValue(code) {
    const textarea = this.getTextarea();
    if (textarea) {
      textarea.value = code || '';
      this.updateLineNumbers();
    }
  }

  getValue() {
    const textarea = this.getTextarea();
    return textarea ? textarea.value.trim() : '';
  }

  clear() {
    const textarea = this.getTextarea();
    if (textarea) {
      textarea.value = '';
      textarea.focus();
      this.updateLineNumbers();
    }
  }

  applyTheme(theme) {
    const editorArea = document.querySelector('.code-editor-pane');
    if (!editorArea) return;
    if (theme === 'dark-monokai') {
      editorArea.style.backgroundColor = '#272822';
    } else if (theme === 'dark-midnight') {
      editorArea.style.backgroundColor = '#020617';
    } else {
      editorArea.style.backgroundColor = '#0d1117';
    }
  }

  applyFontSize(size) {
    const textarea = this.getTextarea();
    const lineCol = this.getLineContainer();
    if (textarea) textarea.style.fontSize = size;
    if (lineCol) lineCol.style.fontSize = size;
  }
}

export const codeEditorComponent = new CodeEditor();
