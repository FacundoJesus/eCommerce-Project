import { Button, FormControl, InputLabel, MenuItem, Select, Tooltip } from "@mui/material";
import { useEffect, useState } from "react";
import { FiArrowDown, FiArrowUp, FiRefreshCw, FiSearch } from "react-icons/fi";
import { useLocation, useNavigate, useSearchParams } from "react-router-dom";

const Filter = ({categories}) => {

    const [searchParams] = useSearchParams();
    const pathName = useLocation().pathname;
    const params = new URLSearchParams(searchParams); 
    const navigate = useNavigate();


    const [category, setCategory] = useState("all");
    const [sortOrder,setSortOrder] = useState("asc"); // ordenar
    const [searchTerm, setSearchTerm] = useState(""); // buscar


    useEffect(() => {
        const currentCategory = searchParams.get("category") || "all";
        const currentSortOrder = searchParams.get("sortby") || "asc";
        const currentSearchTerm = searchParams.get("keyword") || "";


        setCategory(currentCategory);
        setSortOrder(currentSortOrder);
        setSearchTerm(currentSearchTerm);

    },[searchParams]);


    useEffect(() => {

        const handler = setTimeout(() => {
            if(searchTerm) {
                searchParams.set("keyword",searchTerm);
            }else {
                searchParams.delete("keyword");
            }
            navigate(`${pathName}?${searchParams.toString()}`);
        },700);

        return () => {
            clearTimeout(handler);
        }
        

    },[searchParams,searchTerm,navigate,pathName]);

    const handleCategoryChange = (event) => {
        const selectedCategory = event.target.value;
        if(selectedCategory === "all") {
            params.delete("category");
        }
        else {
            params.set("category",selectedCategory);
        }
        navigate(`${pathName}?${params}`); // http://localhost:3000/?sortby=asc&category=Laptops

        setCategory(event.target.value);
    }

    const toggleSortOrder = () => {
        setSortOrder((prevOrder) => {
            const newOrder = (prevOrder === "asc") ? "desc" : "asc";
            params.set("sortby",newOrder);
            navigate(`${pathName}?${params}`); // http://localhost:3000/?sortby=desc
            return newOrder;
        })
    }

    const handleClearFilter = () => {
        navigate({pathname : window.location.pathname}); // http://localhost:3000/
    }

    return (

        <div className="flex lg:flex-row flex-col-reverse lg:justify-between justify-center items-center gap-4">
            {/* Barra de Búsqueda */}
            <div className="relative flex items-center 2xl:w-112.5 sm:w-105 w-full">
                <input 
                       value={searchTerm}
                       onChange={(e) => setSearchTerm(e.target.value)}
                       type="text"
                       placeholder = "Search Products"
                       className="border border-gray-400 text-slate-800 rounded-md py-2 pl-10 pr-4 w-full focus:outline-none focus:ring-2 focus:ring-[#1976d2]"
                />
                <FiSearch className="absolute left-3 text-slate-800 size={20}" />
            </div>

            {/* Búsqueda por Categoría seleccionada */}
            <div className="flex sm:flex-row flex-col gap-4 items-center">
                <FormControl 
                    className="text-sl-800 border-slate-700"
                    variant="outlined"
                    size="small">
                        <InputLabel id="category-select-label">Category</InputLabel>
                        <Select
                            labelId="category-select-label"
                            value={category}
                            onChange={handleCategoryChange}
                            label="Category"
                            className="min-w-30 text-slate-800 border-slate-700">
                            <MenuItem value="all">All</MenuItem>
                            {categories.map((item) => (
                                <MenuItem key={item.categoryId} value={item.categoryName}>
                                    {item.categoryName}
                                </MenuItem>
                            ))}
                        </Select>         
                </FormControl>
                

                {/* Botón de ordenamiento */}
                <Tooltip title="Sorted by price: asc">
                    <Button onClick={toggleSortOrder}
                            variant="contained" 
                            color="primary" 
                            className="flex items-center gap-2 h-10">
                        Sort By
                        {sortOrder === "asc" ? (<FiArrowUp size={20}/>) : (<FiArrowDown size={20}/>)} 
                    </Button>
                </Tooltip>

                {/* Bóton de limpiar filtro */}
                <button
                    onClick= {handleClearFilter}
                    className="flex items-center gap-2 bg-rose-900 hover:bg-rose-700 text-white px-3 py-3 h-10 rounded-md transition duration-300 ease-in shadow-md focus:outline-hidden cursor-pointer">
                    <FiRefreshCw className="font-semibold size={16}"/>
                    <span className="font-semibold">Clear Filter</span>
                </button>
            </div>                
            
        </div>
    )

}

export default Filter;