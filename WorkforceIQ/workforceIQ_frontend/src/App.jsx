import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Login from "./Pages/Login";
import Home from "./Pages/Home";
import AddEmployee from "./Pages/AddEmployee";
import Department from "./Pages/Department";
import DepartmentEmployees from "./Pages/DepartmentEmployees";
import UpdateEmployee from "./Pages/UpdateEmployee";
import AIAnalysis from "./Pages/AIAnalysis";
import PromotionPage from "./Pages/PromotionPage";
import ProtectedRoute from "./components/ProtectedRoute";
import HrRoute from "./components/HrRoute";
import { isSessionValid } from "./auth/auth";

function App() {

  return (

    <BrowserRouter>

      <Routes>

        <Route
          path="/"
          element={isSessionValid() ? <Navigate to="/home" replace /> : <Login />}
        />

        <Route
          path="/home"
          element={
            <ProtectedRoute>
              <Home />
            </ProtectedRoute>
          }
        />

        <Route
          path="/department"
          element={
            <ProtectedRoute>
              <Department />
            </ProtectedRoute>
          }
        />

        <Route
          path="/addemployee"
          element={
            <HrRoute>
              <AddEmployee />
            </HrRoute>
          }
        />

        <Route
          path="/update/:id"
          element={
            <HrRoute>
              <UpdateEmployee />
            </HrRoute>
          }
        />

        <Route
          path="/department/:id"
          element={
            <ProtectedRoute>
              <DepartmentEmployees />
            </ProtectedRoute>
          }
        />

        <Route
          path="/ai-analysis"
          element={
            <ProtectedRoute>
              <AIAnalysis />
            </ProtectedRoute>
          }
        />

        <Route
          path="/promotion"
          element={
            <HrRoute>
              <PromotionPage />
            </HrRoute>
          }
        />
      </Routes>

    </BrowserRouter>

  );
}

export default App;
