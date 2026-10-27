/**
 * @file router.js
 * @description Lightweight, scalable HTTP router supporting path parameters, query strings, and HTTP methods.
 */

class Router {
  constructor() {
    this.routes = [];
  }

  add(method, pathPattern, handler) {
    // Convert path with params like /problems/:id to regex and param names
    const paramNames = [];
    const regexPath = pathPattern.replace(/:([a-zA-Z0-9_]+)/g, (_, name) => {
      paramNames.push(name);
      return '([^/]+)';
    });

    this.routes.push({
      method: method.toUpperCase(),
      pattern: new RegExp(`^${regexPath}$`),
      paramNames,
      handler
    });
  }

  get(path, handler) { this.add('GET', path, handler); }
  post(path, handler) { this.add('POST', path, handler); }
  put(path, handler) { this.add('PUT', path, handler); }
  delete(path, handler) { this.add('DELETE', path, handler); }

  match(method, pathname) {
    for (const route of this.routes) {
      if (route.method !== method.toUpperCase()) continue;
      const match = pathname.match(route.pattern);
      if (match) {
        const params = {};
        route.paramNames.forEach((name, idx) => {
          params[name] = decodeURIComponent(match[idx + 1]);
        });
        return { handler: route.handler, params };
      }
    }
    return null;
  }
}

module.exports = Router;
