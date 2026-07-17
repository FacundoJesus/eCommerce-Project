import { useEffect } from "react";
import ProductCard from "./shared/ProductCard";
import { fetchProducts } from "../store/actions/actions";
import { useDispatch, useSelector } from "react-redux";

const About = () => {

    const dispatch = useDispatch();
    const {products} = useSelector((state) => state.products);

    useEffect(() => {
            dispatch(fetchProducts());
        },[dispatch])

    return(
        <div className="max-w-7xl mx-auto px-4 py-8">
            <h1 className="text-slate-800 text-4xl font-bold text-center mb-12">
                About Us
            </h1>
           <div className="flex flex-col lg:flex-row justify-between items-center mb-12">
                <div className="w-full md:w-1/2 text-center md:text-left">
                    <p className="text-lg mb-4">
                        Welcome to our e-commerce store! We are dedicated to providing the
                        best products and services to our customers. Our mission is to offer
                        a seamless shopping experience while ensuring the highest quality of
                        our offerings.
                    </p>
                </div>

                <div className="w-full md:w-1/2 mb-6 md:mb-0">
                    <img
                        src="/src/assets/about_us.png"
                        alt="About Us"
                        className="w-full h-auto rounded-lg shadow-lg transform transition-transform duration-300 hover:scale-105">
                    </img>
                </div>
           </div>


           <div className="py-7 space-y-8">
                <h1 className="text-slate-800 text-4xl font-bold text-center">
                    Our Products
                </h1>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {products &&
                            products?.slice(0,3)
                                     .map((item,i) => <ProductCard key={i} {...item} about/>)}
                </div>
           </div>
        </div>
    )

}

export default About;