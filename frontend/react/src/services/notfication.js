import { createStandaloneToast } from '@chakra-ui/react'

const { toast } = createStandaloneToast()

const notification = (title, description, status) => {
    toast({title, description, status})
}

export const successNotification = (title, description) => {
    notification(title, description, 'success')
}

export const errorNotification = (title, description) => {
    notification(title, description, 'error')
}

export const infoNotification = (title, description) => {
    notification(title, description, 'info')
}