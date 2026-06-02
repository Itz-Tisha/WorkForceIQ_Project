import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./Pages/Login";
import Home from "./Pages/Home";
import AddEmployee from "./Pages/AddEmployee";
import Department from "./Pages/Department";
import DepartmentEmployees from "./Pages/DepartmentEmployees";
import UpdateEmployee from "./Pages/UpdateEmployee";
import AIAnalysis from "./Pages/AIAnalysis";
import PromotionPage from "./Pages/PromotionPage";

function App() {

  return (

    <BrowserRouter>

      <Routes>

        <Route path="/" element={<Login />} />

        <Route path="/home" element={<Home />} />
		

        <Route path="/department" element={<Department />} />
		<Route path="/addemployee" element={<AddEmployee />} />
		<Route path="/update/:id" element={<UpdateEmployee />} />

        <Route
          path="/department/:id"
          element={<DepartmentEmployees />}
        />
		
		<Route
		    path="/ai-analysis"
		    element={<AIAnalysis />}
		/>
		<Route path="/promotion" element={<PromotionPage />} />
      </Routes>
	  

    </BrowserRouter>

  );
}

export default App;