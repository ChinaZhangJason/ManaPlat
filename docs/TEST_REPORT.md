# Test Report Configuration

## Backend Test Report

### Maven Surefire Plugin Configuration

Add to `backend/pom.xml`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.2</version>
    <configuration>
        <includes>
            <include>**/*Test.java</include>
            <include>**/*Tests.java</include>
        </includes>
        <excludes>
            <exclude>**/IntegrationTest.java</exclude>
        </excludes>
    </configuration>
</plugin>
```

### Generate Coverage Report

```bash
cd backend
mvn clean test jacoco:report
# Report location: target/site/jacoco/index.html
```

## Frontend Test Report

### Vitest Coverage Configuration

Add to `frontend/vite.config.js`:

```javascript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  test: {
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'],
      reportsDirectory: './coverage',
      include: ['src/**/*.{js,vue}'],
      exclude: [
        'src/main.js',
        'src/router/index.js',
        '**/*.spec.js'
      ]
    }
  }
})
```

### Generate Coverage Report

```bash
cd frontend
npm run test:coverage
# Report location: coverage/index.html
```

## CI/CD Integration

### GitHub Actions (.github/workflows/test.yml)

```yaml
name: Test and Coverage

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  backend-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Backend Test
        run: mvn clean test jacoco:report
      - name: Upload Coverage
        uses: codecov/codecov-action@v3

  frontend-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Setup Node
        uses: actions/setup-node@v4
        with:
          node-version: '18'
      - name: Frontend Test
        run: |
          cd frontend
          npm install
          npm run test:coverage
      - name: Upload Coverage
        uses: codecov/codecov-action@v3
```

## Test Coverage Targets

| Module | Target | Current |
|-------|--------|---------|
| Backend Service | 80%+ | - |
| Backend Controller | 70%+ | - |
| Frontend Components | 70%+ | - |
| Overall | 75%+ | - |

## Running Tests

### Backend
```bash
cd backend
mvn test                                    # All tests
mvn test -Dtest=*Test                      # Pattern match
mvn test -Dtest=AuthServiceTest#testLogin  # Single method
mvn test jacoco:report                      # With coverage
```

### Frontend
```bash
cd frontend
npm test                     # All tests
npm run test:watch          # Watch mode
npm run test:coverage       # With coverage
npx playwright test         # E2E tests
```
