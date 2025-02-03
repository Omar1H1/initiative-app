export interface Profile {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    isActive: boolean | null;
    matchList: any | null;
}
