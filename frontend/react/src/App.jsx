import SidebarWithHeader from "./components/shared/Sidebar.jsx";
import { useEffect, useState } from "react";
import { Spinner,  Wrap, WrapItem  } from "@chakra-ui/react";
import {getCustomer} from "./services/client.js";
import CardWithImage from "./components/Card.jsx";
import CustomerCreateDrawerForm from "./components/CustomerCreateDrawerForm.jsx";
import {errorNotification} from "./services/notfication.js";

const App = () => {
    const [customers, setCustomers] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const fetchCustomers = () => {
        setIsLoading(true)
        getCustomer().then(response => {
            setCustomers(response.data)
            console.log(response.data)
        }).catch(err => {
            errorNotification(
                err.code,
                err.response.data.message);
        }).finally(() => {
            setIsLoading(false)
        })
    }
    useEffect(() => {
        fetchCustomers();
    }, [])

    return (
        <SidebarWithHeader>
            {isLoading ? (
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='red.500'
                    size='xl'
                />
            ) : (
                <div>
                    <CustomerCreateDrawerForm
                        fetchCustomers={fetchCustomers}/>
                    <Wrap spacing='40px' justify={"center"}>
                        {customers.map((customer, index) => (
                            <WrapItem key={index}>
                                <CardWithImage
                                    fetchCustomers={fetchCustomers}
                                    {...customer}
                                />
                            </WrapItem>
                        ))}
                    </Wrap>
                </div>
            )}
        </SidebarWithHeader>
    );
}

export default App
