import SidebarWithHeader from "./components/shared/Sidebar.jsx";
import { useEffect, useState } from "react";
import { Spinner,  Wrap, WrapItem  } from "@chakra-ui/react";
import {getCustomer} from "./services/client.js";
import CardWithImage from "./components/Card.jsx";

const App = () => {
    const [customers, setCustomers] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        setIsLoading(true)
        getCustomer().then(response => {
            setCustomers(response.data)
        }).catch(err => {
            console.log(err)
        }).finally(() => {
            setIsLoading(false)
        })
    }, [])

    return (
        <SidebarWithHeader>
            {isLoading ? (
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            ) : (
                <Wrap spacing='40px' justify={"center"}>
                    {customers.map((customer) => (
                    <WrapItem>
                        <CardWithImage
                            {...customer}/>
                    </WrapItem>
                    ))}
                </Wrap>
            )}
        </SidebarWithHeader>
    );
}

export default App
